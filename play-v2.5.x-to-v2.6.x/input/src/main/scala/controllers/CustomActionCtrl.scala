/*
rules = [
  MigrateControllers,
  MigrateActions
]
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.{ExecutionContext, Future}

class CustomActionCtrl @Inject()(implicit ec: ExecutionContext) extends Controller {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action {
    Ok("") // keep comments
  }

  def indexAsync: Action[AnyContent] = Action.async {
    Future {
      Ok("") // keep comments
    }
  }

  def block: Action[AnyContent] = Action {
    {
      Ok("") // keep comments
    }
  }

  def parseSync: Action[JsValue] = Action(parse.json) { _ =>
    Ok("") // keep comments
  }

  def parseAsync: Action[JsValue] = Action.async(parse.json) { _ =>
    Future {
      Ok("") // keep comments
    }
  }

  def named: Action[AnyContent] = Action { req =>
    Ok("") // keep comments
  }

  def implicitNamed: Action[AnyContent] = Action { implicit req =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
