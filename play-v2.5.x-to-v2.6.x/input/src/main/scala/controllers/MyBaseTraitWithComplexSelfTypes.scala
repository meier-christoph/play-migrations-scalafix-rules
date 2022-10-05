/*
rule = MigrateControllers
 */
// format: off
package controllers

import play.api.mvc.Controller

trait MyBaseTraitWithComplexSelfTypes { self: Controller with Dummy1 with Dummy2 =>

}
