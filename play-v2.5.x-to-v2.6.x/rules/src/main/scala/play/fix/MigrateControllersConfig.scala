package play.fix

import metaconfig.ConfDecoder
import metaconfig.generic.Surface

case class MigrateControllersConfig(
    controllerClasses: List[String] = Nil
)
object MigrateControllersConfig {
  val default = MigrateControllersConfig()
  implicit val surface: Surface[MigrateControllersConfig] =
    metaconfig.generic.deriveSurface[MigrateControllersConfig]
  implicit val decoder: ConfDecoder[MigrateControllersConfig] =
    metaconfig.generic.deriveDecoder(default)
}
