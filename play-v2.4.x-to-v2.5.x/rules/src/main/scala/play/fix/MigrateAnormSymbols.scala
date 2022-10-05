package play.fix

import scalafix.v1._

import scala.meta._

final class MigrateAnormSymbols() extends SemanticRule("MigrateAnormSymbols") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
//      case Term.ApplyInfix(t @ Lit.Symbol(sym), Term.Name("->"), _, _) =>
      case t @ Lit.Symbol(sym) =>
        Patch.replaceTree(t, s""""${sym.name}"""")
    }.asPatch
  }
}
