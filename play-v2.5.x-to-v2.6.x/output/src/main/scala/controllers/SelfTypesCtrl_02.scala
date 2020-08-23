// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}
import play.api.mvc.{ BaseController, ControllerComponents }

/** A variation with multiple self types. */
class SelfTypesCtrl_02 @Inject() (val controllerComponents: ControllerComponents) extends BaseController with MyBaseTraitWithComplexSelfTypes with Dummy1 with Dummy2 {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit req =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
