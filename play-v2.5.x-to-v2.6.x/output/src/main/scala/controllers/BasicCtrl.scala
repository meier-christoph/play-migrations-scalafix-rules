// format: off
package controllers

import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent}
import services.SomeServiceWeInjectIntoOurControllers
import play.api.mvc.{ BaseController, ControllerComponents }

// this one is a case class i.e. don't use val for the components
case class BasicCtrl @Inject() (srv: SomeServiceWeInjectIntoOurControllers, controllerComponents: ControllerComponents) extends BaseController with SomeTraitWeNeedToKeep {

  def index: Action[AnyContent] = Action {
    Ok(srv.foo)
  }

  def foo: Action[JsValue] = Action(parse.json) { implicit request =>
    Ok(request.body)
  }

}
