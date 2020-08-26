package play.fix

import scalafix.v1._

import scala.meta._

final class MigrateInjectControllers() extends SemanticRule("MigrateInjectControllers") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    Patch.empty
  }
}
