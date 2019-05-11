/*
rule = MigrateExecutionContext
 */
// format: off
package services

import javax.inject.Inject
import play.api.libs.ws.WSClient
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

class AnOtherServiceToInject @Inject()(wsClient: WSClient) {
  def bar: String = "bar"

  def doWSCall(): Future[Int] = wsClient
    .url("http://example.org")
    .get()
    .map(_.status)
}
