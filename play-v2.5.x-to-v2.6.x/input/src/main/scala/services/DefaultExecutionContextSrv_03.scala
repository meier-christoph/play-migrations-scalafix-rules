/*
rule = MigrateExecutionContext
 */
// format: off
package services

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits._

import scala.concurrent.Future

/** The ExecutionContext should be provided using DI. */
class DefaultExecutionContextSrv_03 @Inject()() {

  // keep comments

  /** keep doc */
  def json: Future[String] = 
    Future {
      "" // keep comments
    }
  
  // keep comments
}

// keep comments
