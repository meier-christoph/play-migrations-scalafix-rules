// format: off
package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import javax.inject.Inject

class Controller_01 @Inject() extends Controller {
  // keep comments

  /** keep doc */
  def index: Action[AnyContent] =
    Action { implicit req =>
      Ok("") // keep comments
    }

  // keep comments
}

// keep comments
