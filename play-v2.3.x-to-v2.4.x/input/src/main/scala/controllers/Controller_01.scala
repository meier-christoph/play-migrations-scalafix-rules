/*
rule = MigrateInjectControllers
 */
// format: off
package controllers

import play.api.mvc.{Action, AnyContent, Controller}

object Controller_01 extends Controller {
  // keep comments

  /** keep doc */
  def index: Action[AnyContent] =
    Action { implicit req =>
      Ok("") // keep comments
    }

  // keep comments
}

// keep comments
