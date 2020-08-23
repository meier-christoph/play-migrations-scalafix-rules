package play.fix

import play.fix.Classes._
import scalafix.v1._

import scala.meta._

class MigrateExecutionContext extends SemanticRule("MigrateExecutionContext") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    val imports = ImportHolder(doc.tree)

    val hasExecutionImport =
      imports.hasImport(importer"play.api.libs.concurrent.Execution.Implicits.defaultContext") ||
        imports.hasImport(importer"play.api.libs.concurrent.Execution.Implicits._") ||
        imports.hasImport(importer"scala.concurrent.ExecutionContext.Implicits.global") ||
        imports.hasImport(importer"scala.concurrent.ExecutionContext.Implicits._")

    imports
      .ensureNot(importer"play.api.libs.concurrent.Execution.Implicits.defaultContext")
      .ensureNot(importer"play.api.libs.concurrent.Execution.Implicits._")
      .ensureNot(importer"scala.concurrent.ExecutionContext.Implicits.global")
      .ensureNot(importer"scala.concurrent.ExecutionContext.Implicits._")

    val ec = Term.Param(
      List(Mod.Implicit()),
      Name("executionContext"),
      Some(Type.Name("ExecutionContext")),
      None
    )

    doc.tree
      .collect {
        case clazz: Defn.Class if hasExecutionImport =>
          val edited = clazz.ensureParam(ec)
          imports.ensure(importer"scala.concurrent.ExecutionContext")
          ExtraPatch.replaceClassDef(clazz, edited)
      }
      .asPatch
      .atomic +
      imports.asPatch
  }
}
