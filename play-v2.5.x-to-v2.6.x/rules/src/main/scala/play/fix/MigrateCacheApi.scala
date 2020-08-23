package play.fix

import play.fix.Symbols._
import scalafix.v1._

import scala.meta._

final class MigrateCacheApi() extends SemanticRule("MigrateCacheApi") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree
      .collect {
        ???
      }
      .asPatch
      .atomic
  }
}
