/*
rule = MigrateControllers
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext

/** I18n has changed slightly, all action should have an implicit request.  */
class I18nSupportCtrl_01 @Inject()(val messagesApi: MessagesApi)(implicit ec: ExecutionContext) extends Controller with I18nSupport {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit req =>
    Ok(Messages("some.key")) // keep comments
  }

  // keep comments
}

// keep comments
