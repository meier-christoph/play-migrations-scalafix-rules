package play.fix

import play.fix.Classes._
import scalafix.v1._

import scala.meta._

class MigrateControllers extends SemanticRule("MigrateControllers") {

  val ec = Term.Param(List(Mod.Implicit()), Name("ec"), Some(Type.Name("ExecutionContext")), None)

  override def fix(implicit doc: SemanticDocument): Patch = {
//    println("Tree.structure: " + doc.tree.structure)
    var hasControllerImport = false
    doc.tree.collect {
      case Importer(q"play.api.mvc", importedTypes) =>
        importedTypes.collect {
          case i @ importee"Controller" =>
            hasControllerImport = true
            Patch.removeImportee(i) +
              Patch.addGlobalImport(importer"play.api.mvc.BaseController") +
              Patch.addGlobalImport(importer"play.api.mvc.ControllerComponents")
        }.asPatch

      case clazz @ Defn.Class(_, _, _, _, Template(_, inits, _, _)) if hasControllerImport && inits.map(_.tpe.syntax).contains("Controller") =>
        val valMod = if (clazz.isCase) Nil else List(Mod.ValParam())
        val ctrlComp = Term.Param(valMod, Name("controllerComponents"), Some(Type.Name("ControllerComponents")), None)
        val baseCtrl = Init(Type.Name("BaseController"), Name.Anonymous(), Nil)
        val ctrl = clazz
          .mapInit(i => baseCtrl +: i.filterNot(_.tpe.syntax == "Controller"))
          .ensureParam(ctrlComp)
        ExtraPatch.replaceClassDef(clazz, ctrl)
    }.asPatch
  }
}
