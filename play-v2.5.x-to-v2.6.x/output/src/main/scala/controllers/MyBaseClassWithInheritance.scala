// format: off
package controllers

import play.api.mvc.BaseController

/** After migration this must be made an abstract class because controllerComponents will be missing. */
abstract class MyBaseClassWithInheritance extends BaseController
