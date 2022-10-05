package play.fix

import scalafix.v1._

final class MigrateWSClient() extends SemanticRule("MigrateWSClient") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    Patch.replaceSymbols(
      "play/api/libs/ws/WSRequest#withQueryString()." -> "play/api/libs/ws/WSRequest#withQueryStringParameters().",
      "play/api/libs/ws/WSRequest#withHeaders()." -> "play/api/libs/ws/WSRequest#addHttpHeaders().",
      "play/api/libs/ws/WSResponse#allHeaders()." -> "play/api/libs/ws/WSResponse#headers()."
    )
  }
}
