// format: off
package controllers

import javax.inject.Inject
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext
import play.api.mvc.{ BaseController, ControllerComponents }

/** I18n has changed slightly, all action should have an implicit request.  */
class I18nSupportCtrl_01 @Inject() (val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController with I18nSupport {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit req =>
    Ok(Messages("some.key")) // keep comments
  }

  // keep comments
}

// keep comments
