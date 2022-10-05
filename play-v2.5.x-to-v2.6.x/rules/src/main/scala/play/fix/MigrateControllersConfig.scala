package play.fix

import metaconfig.ConfDecoder
import metaconfig.generic.Surface

/**
  * Config for MigrateControllers rule.
  * @param controller you can provide your own type given it extends BaseController
  * @param controllerComponents you can provide your own type given it extends ControllerComponents
  * @param abstractControllers instead of injecting components, convert to abstract class
  */
final case class MigrateControllersConfig(
    controller: String = "play.api.mvc.BaseController",
    controllerComponents: String = "play.api.mvc.ControllerComponents",
    abstractControllers: List[String] = Nil
)
object MigrateControllersConfig {
  val default: MigrateControllersConfig = MigrateControllersConfig()
  implicit val surface: Surface[MigrateControllersConfig] =
    metaconfig.generic.deriveSurface[MigrateControllersConfig]
  implicit val decoder: ConfDecoder[MigrateControllersConfig] =
    metaconfig.generic.deriveDecoder(default)
}
