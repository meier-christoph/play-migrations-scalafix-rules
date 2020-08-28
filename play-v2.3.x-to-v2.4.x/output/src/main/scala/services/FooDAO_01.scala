// format: off
package services

import anorm._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WS

import scala.concurrent.Future
import javax.inject.Inject
import play.api.Play
import play.api.db.Database
import play.api.libs.ws.WSClient

class FooDAO_01 @Inject() (val _database: Database, val _wsClient: WSClient) {
  def fetchAll: List[String] = {
    _database.withConnection { implicit conn =>
      SQL("select * from foo").as(SqlParser.str("foo").*)
    }
  }

  def fetchOne: Future[Option[String]] = {
    _wsClient.url("http://127.0.0.1:9000")
      .get()
      .map {
        case r if r.status == 200 => Some(r.body)
        case _                    => None
      }
  }
}
@deprecated("(scalafix) Migrate to DI", "2.3.0")
object FooDAO_01 {
  // FIXME(scalafix): Remove once migration is completed ("services.FooDAO_01")
  lazy val _instance: FooDAO_01 = Play.current.injector.instanceOf[FooDAO_01]
  implicit def _instance(f: FooDAO_01.type): FooDAO_01 = f._instance
}
