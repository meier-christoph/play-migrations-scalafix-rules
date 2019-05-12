// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}
import play.api.mvc.{ BaseController, ControllerComponents }

/** Most ppl use self typed trait to add extra methods to controller. */
class SelfTypesCtrl_01 @Inject() (val controllerComponents: ControllerComponents) extends BaseController with MyBaseTraitWithSelfTypes {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit _request =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
