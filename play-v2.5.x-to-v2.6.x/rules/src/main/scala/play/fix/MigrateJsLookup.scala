package play.fix

import scalafix.v1._

import scala.meta._

final class MigrateJsLookup() extends SemanticRule("MigrateJsLookup") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    val JsLookupImpl = SymbolMatcher.exact("play/api/libs/json/JsValue.jsValueToJsLookup().")
    val JsLookup = SymbolMatcher.exact("play/api/libs/json/JsLookup#apply().")
    doc.tree.collect {
      case t @ JsLookup(Term.Apply(Term.Select(name, Term.Name("apply")), List(idx))) =>
        Patch.replaceTree(t, s"($name \\ $idx)")
      case t @ JsLookupImpl(Term.Apply(name, List(Lit.Int(idx)))) =>
        Patch.replaceTree(t, s"($name \\ $idx)")
    }.asPatch
  }
}
