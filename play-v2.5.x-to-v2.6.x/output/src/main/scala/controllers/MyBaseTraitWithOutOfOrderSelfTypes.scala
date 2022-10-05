// format: off
package controllers

import play.api.mvc.BaseController

trait MyBaseTraitWithOutOfOrderSelfTypes { self: Dummy1 with BaseController with Dummy2 =>

}
