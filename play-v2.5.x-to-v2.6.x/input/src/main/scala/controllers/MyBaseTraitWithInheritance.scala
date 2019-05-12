/*
rule = MigrateControllers
MigrateControllers.controllerClasses = [
  MyBaseTraitWithInheritance
]
 */
// format: off
package controllers

import play.api.mvc.Controller

trait MyBaseTraitWithInheritance extends Controller
