/*
rule = MigrateControllers
 */
// format: off
package controllers

import play.api.mvc.Controller

trait MyBaseTraitWithOutOfOrderSelfTypes { self: Dummy1 with Controller with Dummy2 =>

}
