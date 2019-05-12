/*
rule = MigrateControllers
 */
// format: off
package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Controller}

/** A variation with multiple self types. */
class SelfTypesCtrl_02 @Inject() extends Controller with MyBaseTraitWithComplexSelfTypes with Dummy1 with Dummy2 {

  // keep comments

  /** keep doc */
  def index: Action[AnyContent] = Action {
    Ok("") // keep comments
  }

  // keep comments
}

// keep comments
