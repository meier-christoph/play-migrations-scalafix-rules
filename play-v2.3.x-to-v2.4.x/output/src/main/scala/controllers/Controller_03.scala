// format: off
package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import services.FooDAO_01
import javax.inject.Inject

class Controller_03 @Inject() extends Controller {
  // keep comments

  /** keep doc */
  def index: Action[AnyContent] =
    Action { implicit req =>
      val foo = FooDAO_01.fetchAll
      Ok("") // keep comments
    }

  // keep comments
}

// keep comments
