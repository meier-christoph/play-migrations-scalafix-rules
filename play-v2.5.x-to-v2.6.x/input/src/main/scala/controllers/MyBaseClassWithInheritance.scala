/*
rule = MigrateControllers
MigrateControllers.controllerClasses = [
  MyBaseClassWithInheritance
]
 */
// format: off
package controllers

import play.api.mvc.Controller

/** After migration this must be made an abstract class because controllerComponents will be missing. */
class MyBaseClassWithInheritance extends Controller
