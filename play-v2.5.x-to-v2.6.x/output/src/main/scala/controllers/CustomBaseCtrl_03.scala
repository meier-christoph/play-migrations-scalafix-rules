// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}
import play.api.mvc.ControllerComponents

/** Sometimes they use their own base controller trait or abstract class via inheritance. */
class CustomBaseCtrl_03 @Inject() (val controllerComponents: ControllerComponents) extends MyBaseClassWithInheritance {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit _request =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
