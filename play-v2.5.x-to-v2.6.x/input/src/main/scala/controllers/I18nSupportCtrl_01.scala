/*
rule = MigrateControllers
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.{ExecutionContext, Future}

/** I18n has changed slightly, all action should have an implicit request.  */
class I18nSupportCtrl_01 @Inject()(val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action {
    Ok(Messages("some.key")) // keep comments
  }

  def indexAsync: Action[AnyContent] = Action.async {
    Future {
      Ok(Messages("some.key")) // keep comments
    }
  }

  def block: Action[AnyContent] = Action {
    {
      Ok(Messages("some.key")) // keep comments
    }
  }

  def parseSync: Action[JsValue] = Action(parse.json) { _ =>
      Ok(Messages("some.key")) // keep comments
  }

  def parseAsync: Action[JsValue] = Action.async(parse.json) { _ =>
    Future {
      Ok(Messages("some.key")) // keep comments
    }
  }

  def named: Action[AnyContent] = Action { req =>
    Ok(Messages("some.key")) // keep comments
  }

  def implicitNamed: Action[AnyContent] = Action { implicit req =>
    Ok(Messages("some.key")) // keep comments
  }

  // keep comments
}

// keep comments
