/*
rule = MigrateControllers
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Controller}

/** A variation with the Controller at a random place in the self types. */
class SelfTypesCtrl_03 @Inject() extends Controller with MyBaseTraitWithOutOfOrderSelfTypes with Dummy1 with Dummy2 {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit req =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
