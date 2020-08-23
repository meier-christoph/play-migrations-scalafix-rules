package play.fix

import scalafix.v1._

import scala.meta._

final class MigrateCacheApi() extends SemanticRule("MigrateCacheApi") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    object CacheApi {
      def unapply(t: Term): Option[Term] = {
        if (t.symbol.owner.value == "play/api/cache/CacheApi#") {
          Some(t)
        } else None
      }
    }

    doc.tree
      .collect {
        case t @ CacheApi(Term.ApplyType(Term.Select(field, Term.Name("getOrElse")), List(tpe))) =>
          Patch.replaceTree(t, s"$field.getOrElseUpdate[$tpe]")
      }
      .asPatch
      .atomic +
      Patch.replaceSymbols(
        "play.api.cache.CacheApi" -> "play.api.cache.SyncCacheApi"
      )
  }
}
