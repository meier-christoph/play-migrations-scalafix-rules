// format: off
package controllers

import javax.inject.Inject
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent}
import services.{AnOtherServiceToInject, AndYetAnOtherServiceToInject, SomeServiceWeInjectIntoOurControllers}
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{ BaseController, ControllerComponents }

class AdvancedCtrl @Inject() (srv: SomeServiceWeInjectIntoOurControllers, other: AnOtherServiceToInject, val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext, extra: AndYetAnOtherServiceToInject) extends BaseController with SomeTraitWeNeedToKeep with SomeTypedTraitWeNeedToKeep[JsValue] {

  def index: Action[AnyContent] = Action {
    Ok(srv.foo)
  }

  // comments must survive

  def foo: Action[JsValue] = Action(parse.json) { implicit request =>
    Ok(request.body)
  }

  /**
    * Doc must survive.
    *
    * @return a response.
    */
  def bar: Action[JsValue] = Action.async(parse.json) { implicit request =>
    Future {
      Ok(request.body)
    }
  }

  /*
   * Multiple
   * lines
   * of
   * comments
   * must
   * be
   * ok
   * too.
   */

  override def something: JsValue = Json.obj()
}
