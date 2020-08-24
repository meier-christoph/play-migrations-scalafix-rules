// format: off
package services

import javax.inject.Inject
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class WSClient_01 @Inject() (ws: WSClient)(implicit ec: ExecutionContext) {
  def foo(): Future[Int] = {
    ws.url("http://127.0.0.1:9000")
      .withQueryStringParameters(
        "_q" -> "foo"
      )
      .addHttpHeaders(
        "X-Foo" -> "bar"
      )
      .get()
      .map(_.headers.size)
  }
}
