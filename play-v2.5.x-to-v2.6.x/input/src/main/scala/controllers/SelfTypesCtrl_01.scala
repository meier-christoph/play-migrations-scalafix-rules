/*
rule = MigrateControllers
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Controller}

/** Most ppl use self typed trait to add extra methods to controller. */
class SelfTypesCtrl_01 @Inject() extends Controller with MyBaseTraitWithSelfTypes {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action { implicit req =>
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
