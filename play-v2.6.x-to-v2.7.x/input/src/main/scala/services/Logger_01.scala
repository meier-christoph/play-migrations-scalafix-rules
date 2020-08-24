/*
rule = MigrateLogger
 */
// format: off
package services

import javax.inject.Inject
import play.api.Logger

import scala.util.control.NonFatal

class Logger_01 @Inject()() extends Dummy {
  Logger.info("lorem ipsum")
  
  class Foo {
    def xxx :String = ???
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
object Logger_01 {
  Logger.debug("companion")
}
