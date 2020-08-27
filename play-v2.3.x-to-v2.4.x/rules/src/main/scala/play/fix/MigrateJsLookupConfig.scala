package play.fix

import metaconfig.ConfDecoder
import metaconfig.generic.Surface

/**
  * Config for MigrateJsLookup rule.
  *
  * @param allowed methods to leave unchanged
  */
final case class MigrateJsLookupConfig(
    allowed: List[String] = Nil
)
object MigrateJsLookupConfig {
  val default: MigrateJsLookupConfig = MigrateJsLookupConfig()
  implicit val surface: Surface[MigrateJsLookupConfig] =
    metaconfig.generic.deriveSurface[MigrateJsLookupConfig]
  implicit val decoder: ConfDecoder[MigrateJsLookupConfig] =
    metaconfig.generic.deriveDecoder(default)
}
