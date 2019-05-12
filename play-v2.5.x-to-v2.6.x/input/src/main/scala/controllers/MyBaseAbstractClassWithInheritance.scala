/*
rule = MigrateControllers
MigrateControllers.controllerClasses = [
  MyBaseAbstractClassWithInheritance
]
 */
// format: off
package controllers

import play.api.mvc.Controller

abstract class MyBaseAbstractClassWithInheritance(d: Dummy) extends Controller
