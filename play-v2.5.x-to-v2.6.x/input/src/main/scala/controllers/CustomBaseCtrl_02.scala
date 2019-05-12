/*
rule = MigrateControllers
MigrateControllers.controllerClasses = [
  MyBaseAbstractClassWithInheritance
]
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}

/** Sometimes they use their own base controller trait or abstract class via inheritance. */
class CustomBaseCtrl_02 @Inject()(d: Dummy) extends MyBaseAbstractClassWithInheritance(d) {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action {
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
