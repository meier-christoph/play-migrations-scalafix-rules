// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}
import play.api.mvc.{ BaseController, ControllerComponents }

class SimpleCtrl @Inject() (val controllerComponents: ControllerComponents) extends BaseController {
  def index: Action[AnyContent] = Action {
    Ok("")
  }
}
