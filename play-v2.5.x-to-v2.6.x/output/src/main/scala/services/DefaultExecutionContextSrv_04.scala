// format: off
package services

import javax.inject.Inject

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

/** The ExecutionContext should be provided using DI. */
class DefaultExecutionContextSrv_04 @Inject() ()(implicit executionContext: ExecutionContext) {

  // keep comments

  /** keep doc */
  def json: Future[String] =
    Future {
      "" // keep comments
    }

  // keep comments
}

// keep comments
