/*
rule = MigrateLogger
 */
// format: off
package services

import play.api.Logger

import scala.util.control.NonFatal

trait Logger_02 extends Dummy {
  Logger.info("lorem ipsum")

  class Foo {
    def xxx: String = ???
  }

  class Bar {
    Logger.trace("bar")
  }

  def foo(): Unit = {
    Logger.warn("foo")
  }

  def error(): Unit = {
    try {
      ???
    } catch {
      case NonFatal(err) => Logger.error(err.getMessage, err)
    }
  }
}
object Logger_02 {
  Logger.debug("companion")
}
