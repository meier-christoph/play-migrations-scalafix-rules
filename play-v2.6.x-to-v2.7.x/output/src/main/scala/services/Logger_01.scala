// format: off
package services

import javax.inject.Inject
import play.api.Logger

import scala.util.control.NonFatal
import play.api.Logging

class Logger_01 @Inject() () extends Dummy with Logging {
  logger.info("lorem ipsum")

  class Foo {
    def xxx :String = ???
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
object Logger_01 extends Logging {
  logger.debug("companion")
}
