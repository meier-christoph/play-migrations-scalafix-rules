// format: off
package controllers

import play.api.mvc.BaseController

trait MyBaseTraitWithComplexSelfTypes { self: BaseController with Dummy1 with Dummy2 =>

}
