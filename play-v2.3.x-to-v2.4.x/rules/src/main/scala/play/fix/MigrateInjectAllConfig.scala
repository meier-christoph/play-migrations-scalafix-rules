package play.fix

import metaconfig.ConfDecoder
import metaconfig.generic.Surface

/**
  * Config for MigrateInjectAll rule.
  *
  * @param types to convert for DI, if empty run on play types (WS, DB, Cache, ect.)
  */
final case class MigrateInjectAllConfig(
    types: List[String] = Nil
)
object MigrateInjectAllConfig {
  val default: MigrateInjectAllConfig = MigrateInjectAllConfig()
  implicit val surface: Surface[MigrateInjectAllConfig] =
    metaconfig.generic.deriveSurface[MigrateInjectAllConfig]
  implicit val decoder: ConfDecoder[MigrateInjectAllConfig] =
    metaconfig.generic.deriveDecoder(default)
}
