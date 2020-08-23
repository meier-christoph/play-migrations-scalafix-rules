/*
rule = MigrateControllers
 */
// format: off
package controllers

import play.api.mvc.{Action, AnyContent, Controller}

/** Must either use @Inject or a no args class. */
class NoArgsCtrl_01 extends Controller {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit req =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
