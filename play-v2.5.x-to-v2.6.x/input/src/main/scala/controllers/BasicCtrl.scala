/*
rule = MigrateControllers
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent, Controller}
import services.SomeServiceWeInjectIntoOurControllers

class BasicCtrl @Inject()(srv: SomeServiceWeInjectIntoOurControllers) extends Controller with SomeTraitWeNeedToKeep {

  def index: Action[AnyContent] = Action {
    Ok(srv.foo)
  }

  def foo: Action[JsValue] = Action(parse.json) { implicit request =>
    Ok(request.body)
  }

}
