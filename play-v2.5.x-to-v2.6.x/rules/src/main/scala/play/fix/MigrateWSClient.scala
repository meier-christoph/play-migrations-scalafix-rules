package play.fix

import scalafix.v1._

import scala.meta._

final class MigrateWSClient() extends SemanticRule("MigrateWSClient") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree
      .collect {
        case t @ Term.ApplyType(Term.Select(field, Term.Name("getOrElse")), List(tpe)) =>
          Patch.empty
      }
      .asPatch
      .atomic
  }
}
