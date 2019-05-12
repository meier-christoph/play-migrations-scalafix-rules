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
    val baseCtrl = Init(Type.Name("BaseController"), Name.Anonymous(), Nil)
    def replaceController(i: List[Init]): List[Init] = {
      baseCtrl +: i.filterNot(_.tpe.syntax == "Controller")
    }

    var hasControllerImport = false
    doc.tree.collect {
      case Importer(q"play.api.mvc", importedTypes) =>
        importedTypes.collect {
          case i @ importee"Controller" =>
            hasControllerImport = true
            Patch.removeImportee(i)
        }.asPatch

      case t @ Defn.Class(_, n, _, _, Template(_, inits, _, _))
          if hasControllerImport &&
            inits.map(_.tpe.syntax).contains("Controller") &&
            !config.controllerClasses.contains(n.syntax) =>
        val valMod = if (t.isCase) Nil else List(Mod.ValParam())
        val ctrlComp = Term.Param(valMod, Name("controllerComponents"), Some(Type.Name("ControllerComponents")), None)
        val fixed = t
          .mapInit(replaceController)
          .ensureParam(ctrlComp)
        ExtraPatch.replaceClassDef(t, fixed) +
          Patch.addGlobalImport(importer"play.api.mvc.BaseController") +
          Patch.addGlobalImport(importer"play.api.mvc.ControllerComponents")

      case t @ Defn.Class(_, _, _, _, Template(_, inits, _, _))
          if hasControllerImport &&
            inits.map(_.tpe.syntax).contains("Controller") =>
        val fixed = t.mapInit(replaceController).ensureMod(Mod.Abstract())
        ExtraPatch.replaceClassDef(t, fixed) +
          Patch.addGlobalImport(importer"play.api.mvc.BaseController")

      case t @ Defn.Class(_, _, _, _, Template(_, inits, _, _)) if inits.map(_.tpe.syntax).exists(tpe => config.controllerClasses.contains(tpe)) =>
        val valMod = if (t.isCase) Nil else List(Mod.ValParam())
        val ctrlComp = Term.Param(valMod, Name("controllerComponents"), Some(Type.Name("ControllerComponents")), None)
        val fixed = t.ensureParam(ctrlComp)
        ExtraPatch.replaceClassDef(t, fixed) +
          Patch.addGlobalImport(importer"play.api.mvc.ControllerComponents")

      case t @ Defn.Trait(_, _, _, _, Template(_, inits, _, _))
          if hasControllerImport &&
            inits.map(_.tpe.syntax).contains("Controller") =>
        val fixed = t.mapInit(replaceController)
        ExtraPatch.replaceTraitDef(t, fixed) +
          Patch.addGlobalImport(importer"play.api.mvc.BaseController")

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
        Patch.replaceTree(self, fixed.syntax) +
          Patch.addGlobalImport(importer"play.api.mvc.BaseController")
    }.asPatch
  }
}
