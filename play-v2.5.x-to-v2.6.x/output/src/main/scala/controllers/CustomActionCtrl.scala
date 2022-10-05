// format: off
package controllers

import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{ BaseController, ControllerComponents }

class CustomActionCtrl @Inject() (val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit _request =>
    Ok("") // keep comments
  }

  def indexAsync: Action[AnyContent] = Action.async { implicit _request =>
    Future {
      Ok("") // keep comments
    }
  }

  def block: Action[AnyContent] = Action { implicit _request =>
    {
      Ok("") // keep comments
    }
  }

  def parseSync: Action[JsValue] = Action(parse.json) { implicit _request =>
    Ok("") // keep comments
  }

  def parseAsync: Action[JsValue] = Action.async(parse.json) { implicit _request =>
    Future {
      Ok("") // keep comments
    }
  }

  def named: Action[AnyContent] = Action { implicit req =>
    Ok("") // keep comments
  }

  def implicitNamed: Action[AnyContent] = Action { implicit req =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
