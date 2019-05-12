/*
rule = MigrateControllers
MigrateControllers.controllerClasses = [
  MyBaseTraitWithInheritance
]
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}

/** Sometimes they use their own base controller trait or abstract class via inheritance. */
class CustomBaseCtrl_01 @Inject() extends MyBaseTraitWithInheritance {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action {
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
