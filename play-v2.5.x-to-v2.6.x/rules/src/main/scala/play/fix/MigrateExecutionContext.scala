package play.fix

import play.fix.Classes._
import scalafix.v1._

import scala.meta._

class MigrateExecutionContext extends SemanticRule("MigrateExecutionContext") {

  override def fix(implicit doc: SemanticDocument): Patch = {
//    println("Tree.structure: " + doc.tree.structure)
    var hasExecutionImport = false
    doc.tree
      .collect {
        case Importer(q"play.api.libs.concurrent.Execution.Implicits", importedTypes) =>
          importedTypes.collect {
            case i @ importee"defaultContext" =>
              hasExecutionImport = true
              Patch.removeImportee(i)
          }.asPatch

        case Importer(q"scala.concurrent.ExecutionContext.Implicits", importedTypes) =>
          importedTypes.collect {
            case i @ importee"global" =>
              hasExecutionImport = true
              Patch.removeImportee(i)
          }.asPatch

        case clazz: Defn.Class if hasExecutionImport =>
          val ec = Term.Param(List(Mod.Implicit()), Name("executionContext"), Some(Type.Name("ExecutionContext")), None)
          val edited = clazz.ensureParam(ec)
          ExtraPatch.replaceClassDef(clazz, edited) +
            Patch.addGlobalImport(importer"scala.concurrent.ExecutionContext")
      }
      .asPatch
      .atomic
  }
}
