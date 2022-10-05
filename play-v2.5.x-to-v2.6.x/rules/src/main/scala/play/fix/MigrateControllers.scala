package play.fix

import metaconfig.Configured
import scalafix.v1._

import scala.meta._

final class MigrateControllers(config: MigrateControllersConfig) extends SemanticRule("MigrateControllers") {
  def this() = this(MigrateControllersConfig.default)

  override def withConfiguration(config: Configuration): Configured[Rule] = {
    config.conf
      .getOrElse("MigrateControllers")(this.config)
      .map(newConfig => new MigrateControllers(newConfig))
  }

  override def fix(implicit doc: SemanticDocument): Patch = {
    object Controller {
      val sym: Symbol = Symbols.fromFQCN("play.api.mvc.Controller")

      val f = new TypeFinder("Controller")
      def unapply(t: Type): Boolean =
        f.unapply(t)

      def unapply(t: Term): Boolean =
        t.isInstanceOfType(sym)

      def unapply(l: List[Init]): Option[String] = {
        l.find {
          case Init(t, _, _) =>
            t.symbol.isInstanceOfType(sym)
          case _ =>
            false
        }.map(_.name.syntax)
      }
    }

    val controllerSymbol = Symbols.fromFQCN(config.controller)
    val controllerComponentsSymbol = Symbols.fromFQCN(config.controllerComponents)

    val imports = ImportHolder(doc.tree)

    val inject = Mod.Annot(Init(Type.Name("Inject"), Name.Anonymous(), List(Nil)))
    val baseCtrl = Init(controllerSymbol.toType, Name.Anonymous(), Nil)

    def replaceCtrl(i: List[Init]): List[Init] = {
      i.map {
        case t if t.tpe.syntax == "Controller" => baseCtrl
        case other                             => other
      }
    }

    def replaceCtrlInSelfTypes(t: Type): Type =
      t match {
        case Type.Name("Controller") => controllerSymbol.toType
        case Type.With(l, r)         => Type.With(replaceCtrlInSelfTypes(l), replaceCtrlInSelfTypes(r))
        case other                   => other
      }

    def makeControllerComponents(isVal: Boolean): Term.Param = {
      Term.Param(if (isVal) List(Mod.ValParam()) else Nil, Name("controllerComponents"), Some(controllerComponentsSymbol.toType), None)
    }

    doc.tree
      .collect {
        case t @ Defn.Class(m, n, _, _, Template(_, inits, _, _))
            if inits.map(_.tpe.syntax).contains("Controller") &&
              (config.abstractControllers.contains(n.syntax) || Mods.isAbstract(m)) =>
          val fixed = t
            .mapInit(replaceCtrl)
            .ensureMod(Mod.Abstract())
          imports
            .ensureNot(importer"play.api.mvc.Controller")
            .ensure(controllerSymbol.toImporter)
          ExtraPatch.replaceClassDef(t, fixed)

        case t @ Defn.Class(_, _, _, _, Template(_, inits, _, _)) if inits.map(_.tpe.syntax).contains("Controller") =>
          val fixed = t
            .mapInit(replaceCtrl)
            .ensureParam(makeControllerComponents(!t.isCase))
            .ensureAnnot(inject)
            .removeParamByName(className = Some("MessagesApi"))

          imports
            .ensureNot(importer"play.api.mvc.Controller")
            .ensure(importer"javax.inject.Inject")
            .ensure(controllerSymbol.toImporter)
            .ensure(controllerComponentsSymbol.toImporter)
          ExtraPatch.replaceClassDef(t, fixed)

        case t @ Defn.Class(_, _, _, _, Template(_, Controller(_), _, _)) =>
          val fixed = t
            .ensureParam(makeControllerComponents(!t.isCase))
            .ensureAnnot(inject)
          imports
            .ensure(importer"javax.inject.Inject")
            .ensure(controllerComponentsSymbol.toImporter)
          ExtraPatch.replaceClassDef(t, fixed)

        case t @ Defn.Trait(_, _, _, _, Template(_, inits, _, _)) if inits.map(_.tpe.syntax).contains("Controller") =>
          val fixed = t.mapInit(replaceCtrl)
          imports
            .ensureNot(importer"play.api.mvc.Controller")
            .ensure(controllerSymbol.toImporter)
          ExtraPatch.replaceTraitDef(t, fixed)

        case Defn.Trait(_, _, _, _, Template(_, _, self, _)) if self.decltpe.exists(Controller.unapply) =>
          val fixed = self.copy(decltpe = self.decltpe.map(replaceCtrlInSelfTypes))
          imports
            .ensureNot(importer"play.api.mvc.Controller")
            .ensure(controllerSymbol.toImporter)
          Patch.replaceTree(self, fixed.syntax)
      }
      .asPatch
      .atomic +
      imports.asPatch
  }
}
