/*
rule = MigrateControllers
 */
// format: off
package controllers

import play.api.mvc.Controller

trait MyBaseTraitWithSelfTypes { self: Controller =>

}
