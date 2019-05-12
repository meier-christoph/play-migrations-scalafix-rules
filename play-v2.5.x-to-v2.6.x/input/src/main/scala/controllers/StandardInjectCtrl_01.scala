/*
rule = MigrateControllers
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Controller}

/** Simplest and most basic controller .*/
class StandardInjectCtrl_01 @Inject() extends Controller {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action {
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
