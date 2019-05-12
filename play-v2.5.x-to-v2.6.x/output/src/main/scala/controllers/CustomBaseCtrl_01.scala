// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}
import play.api.mvc.ControllerComponents

/** Sometimes they use their own base controller trait or abstract class via inheritance. */
class CustomBaseCtrl_01 @Inject() (val controllerComponents: ControllerComponents) extends MyBaseTraitWithInheritance {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action {
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
