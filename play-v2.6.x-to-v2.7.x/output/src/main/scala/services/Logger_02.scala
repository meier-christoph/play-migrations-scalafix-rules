// format: off
package services

import play.api.Logger

import scala.util.control.NonFatal
import play.api.Logging

trait Logger_02 extends Dummy with Logging {
  logger.info("lorem ipsum")

  class Foo {
    def xxx: String = ???
  }

  class Bar {
    logger.trace("bar")
  }

  def foo(): Unit = {
    logger.warn("foo")
  }

  def error(): Unit = {
    try {
      ???
    } catch {
      case NonFatal(err) => logger.error(err.getMessage, err)
    }
  }
}
object Logger_02 extends Logging {
  logger.debug("companion")
}
