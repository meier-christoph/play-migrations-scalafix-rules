/*
rule = MigrateControllers
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Controller}

/** Still very basic but with some params and mixed in traits. */
class StandardInjectCtrl_02 @Inject()(d1: Dummy1, dummy2: Dummy2)(implicit d3: Dummy3) extends Controller with Dummy4 with Dummy5 {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit req =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
