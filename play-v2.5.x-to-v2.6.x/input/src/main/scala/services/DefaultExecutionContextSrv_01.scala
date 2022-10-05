/*
rule = MigrateExecutionContext
 */
// format: off
package services

import javax.inject.Inject
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/** The ExecutionContext should be provided using DI. */
class DefaultExecutionContextSrv_01 @Inject()() {

  // keep comments

  /** keep doc */
  def json: Future[String] = 
    Future {
      "" // keep comments
    }
  
  // keep comments
}

// keep comments
