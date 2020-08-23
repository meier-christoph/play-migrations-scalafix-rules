/*
rule = MigrateControllers
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Controller}

/** Same but as case class and no implicit params. */
case class StandardInjectCtrl_03 @Inject()(d1: Dummy1, dummy2: Dummy2) extends Controller with Dummy4 with Dummy5 {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit req =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
