// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Controller}
import services.FooDAO_01

class Controller_02 @Inject() (val _FooDAO_01: FooDAO_01) extends Controller {
  // keep comments

  /** keep doc */
  def index: Action[AnyContent] =
    Action { implicit req =>
      val foo = _FooDAO_01.fetchAll
      Ok("") // keep comments
    }

  // keep comments
}

// keep comments
