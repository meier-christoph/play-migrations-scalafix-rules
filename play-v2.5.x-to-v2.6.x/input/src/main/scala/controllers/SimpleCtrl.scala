/*
rule = MigrateControllers
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Controller}

class SimpleCtrl @Inject() extends Controller {
  def index: Action[AnyContent] = Action {
    Ok("")
  }
}
