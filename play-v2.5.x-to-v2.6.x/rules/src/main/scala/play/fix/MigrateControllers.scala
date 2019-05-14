package play.fix

import metaconfig.Configured
import play.fix.Classes._
import play.fix.Traits._
import scalafix.v1._

import scala.meta._

class MigrateControllers(config: MigrateControllersConfig) extends SemanticRule("MigrateControllers") {
  def this() = this(MigrateControllersConfig.default)

  override def withConfiguration(config: Configuration): Configured[Rule] = {
    config.conf
      .getOrElse("MigrateControllers")(this.config)
      .map(newConfig => new MigrateControllers(newConfig))
  }

  override def fix(implicit doc: SemanticDocument): Patch = {
//    println("Tree.structure: " + doc.tree.structure)
    //

    val ControllerFinder = new TypeFinder("Controller")
    val injectAnnot = Mod.Annot(Init(Type.Name("Inject"), Name.Anonymous(), List(List())))
    val baseCtrl = Init(Type.Name("BaseController"), Name.Anonymous(), Nil)
    def replaceController(i: List[Init]): List[Init] = {
      baseCtrl +: i.filterNot(_.tpe.syntax == "Controller")
    }

    def patchActionWithImplicitRequestArg(b: Term.Block): Patch =
      b.stats.headOption match {
        case Some(fn: Term.Function) =>
          fn match {
            case Term.Function(List(Term.Param(Nil, n @ Term.Name(_), _, _)), _) =>
              Patch.addLeft(n, "implicit ")
            case Term.Function(List(Term.Param(Nil, n @ Name.Anonymous(), _, _)), _) =>
              Patch.addLeft(n, "implicit _request")
            case _ =>
              Patch.empty
          }
        case _ =>
          Patch.addRight(b.tokens.head, " implicit _request =>")
      }

    val imports = Imports(doc.tree)
    val hasControllerImport = imports.hasImport(importer"play.api.mvc.Controller")
    imports.ensureNotImport(importer"play.api.mvc.Controller")
    doc.tree
      .collect {
        case t @ Defn.Class(_, n, _, _, Template(_, inits, _, _))
            if hasControllerImport &&
              inits.map(_.tpe.syntax).contains("Controller") &&
              !config.controllerClasses.contains(n.syntax) =>
          val valMod = if (t.isCase) Nil else List(Mod.ValParam())
          val ctrlComp = Term.Param(valMod, Name("controllerComponents"), Some(Type.Name("ControllerComponents")), None)
          val messagesApi = Term.Param(valMod, Name("messagesApi"), Some(Type.Name("MessagesApi")), None)
          val fixed = t
            .mapInit(replaceController)
            .ensureParam(ctrlComp)
            .ensureAnnot(injectAnnot)
            .ensureNotParam(messagesApi)
          imports
            .ensureImport(importer"javax.inject.Inject")
            .ensureImport(importer"play.api.mvc.BaseController")
            .ensureImport(importer"play.api.mvc.BaseController")
            .ensureImport(importer"play.api.mvc.ControllerComponents")
          ExtraPatch.replaceClassDef(t, fixed)

        case t @ Defn.Class(_, _, _, _, Template(_, inits, _, _))
            if hasControllerImport &&
              inits.map(_.tpe.syntax).contains("Controller") =>
          val fixed = t
            .mapInit(replaceController)
            .ensureMod(Mod.Abstract())
          imports
            .ensureImport(importer"play.api.mvc.BaseController")
          ExtraPatch.replaceClassDef(t, fixed)

        case t @ Defn.Class(_, _, _, _, Template(_, inits, _, _)) if inits.map(_.tpe.syntax).exists(tpe => config.controllerClasses.contains(tpe)) =>
          val valMod = if (t.isCase) Nil else List(Mod.ValParam())
          val ctrlComp = Term.Param(valMod, Name("controllerComponents"), Some(Type.Name("ControllerComponents")), None)
          val messagesApi = Term.Param(valMod, Name("messagesApi"), Some(Type.Name("MessagesApi")), None)
          val fixed = t
            .ensureParam(ctrlComp)
            .ensureAnnot(injectAnnot)
            .ensureNotParam(messagesApi)
          imports
            .ensureImport(importer"javax.inject.Inject")
            .ensureImport(importer"play.api.mvc.ControllerComponents")
          ExtraPatch.replaceClassDef(t, fixed)

        case t @ Defn.Trait(_, _, _, _, Template(_, inits, _, _))
            if hasControllerImport &&
              inits.map(_.tpe.syntax).contains("Controller") =>
          val fixed = t
            .mapInit(replaceController)
          imports
            .ensureImport(importer"play.api.mvc.BaseController")
          ExtraPatch.replaceTraitDef(t, fixed)

        case Defn.Trait(_, _, _, _, Template(_, _, self, _))
            if hasControllerImport &&
              self.decltpe.exists {
                case ControllerFinder() => true
                case _                  => false
              } =>
          def replaceCtrl(t: Type): Type = t match {
            case Type.Name("Controller") => Type.Name("BaseController")
            case Type.With(l, r)         => Type.With(replaceCtrl(l), replaceCtrl(r))
            case other                   => other
          }
          val fixed = self.copy(decltpe = self.decltpe.map(replaceCtrl))
          imports
            .ensureImport(importer"play.api.mvc.BaseController")
          Patch.replaceTree(self, fixed.syntax)

        case Term.Apply(Term.Apply(Term.Select(Term.Name("Action"), _), _), List(b: Term.Block)) =>
          patchActionWithImplicitRequestArg(b)
        case Term.Apply(Term.Apply(Term.Name("Action"), _), List(b: Term.Block)) =>
          patchActionWithImplicitRequestArg(b)
        case Term.Apply(Term.Select(Term.Name("Action"), _), List(b: Term.Block)) =>
          patchActionWithImplicitRequestArg(b)
        case Term.Apply(Term.Name("Action"), List(b: Term.Block)) =>
          patchActionWithImplicitRequestArg(b)
      }
      .asPatch
      .atomic +
      imports.asPatch
  }
}
