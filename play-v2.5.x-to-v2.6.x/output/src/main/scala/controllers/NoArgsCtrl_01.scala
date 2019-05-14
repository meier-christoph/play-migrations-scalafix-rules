// format: off
package controllers

import play.api.mvc.{Action, AnyContent}
import javax.inject.Inject
import play.api.mvc.{ BaseController, ControllerComponents }

/** Must either use @Inject or a no args class. */
class NoArgsCtrl_01 @Inject() (val controllerComponents: ControllerComponents) extends BaseController {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit _request =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
