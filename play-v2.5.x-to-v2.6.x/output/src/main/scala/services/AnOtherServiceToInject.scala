// format: off
package services

import javax.inject.Inject
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

class AnOtherServiceToInject @Inject() (wsClient: WSClient)(implicit executionContext: ExecutionContext) {
  def bar: String = "bar"

  def doWSCall(): Future[Int] = wsClient
    .url("http://example.org")
    .get()
    .map(_.status)
}
