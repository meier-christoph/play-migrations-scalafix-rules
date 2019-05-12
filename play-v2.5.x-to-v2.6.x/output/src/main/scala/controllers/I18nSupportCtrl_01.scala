// format: off
package controllers

import javax.inject.Inject
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent}
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{ BaseController, ControllerComponents }

/** I18n has changed slightly, all action should have an implicit request.  */
class I18nSupportCtrl_01 @Inject() (val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController with I18nSupport {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit _request =>
    Ok(Messages("some.key")) // keep comments
  }

  def indexAsync: Action[AnyContent] = Action.async { implicit _request =>
    Future {
      Ok(Messages("some.key")) // keep comments
    }
  }

  def block: Action[AnyContent] = Action { implicit _request =>
    {
      Ok(Messages("some.key")) // keep comments
    }
  }

  def parseSync: Action[JsValue] = Action(parse.json) { implicit _request_ =>
    Ok(Messages("some.key")) // keep comments
  }

  def parseAsync: Action[JsValue] = Action.async(parse.json) { implicit _request_ =>
    Future {
      Ok(Messages("some.key")) // keep comments
    }
  }

  def named: Action[AnyContent] = Action { implicit req =>
    Ok(Messages("some.key")) // keep comments
  }

  def implicitNamed: Action[AnyContent] = Action { implicit req =>
    Ok(Messages("some.key")) // keep comments
  }

  // keep comments
}

// keep comments
