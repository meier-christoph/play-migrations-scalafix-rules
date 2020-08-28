// format: off
package services

import anorm._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WS

import scala.concurrent.Future
import play.api.Play
import play.api.db.Database
import play.api.libs.ws.WSClient

trait FooDAO_03 {

  // keep comments

  /** keep doc */
  def fetchAll: List[String] = {
    _database.withConnection { implicit conn =>
      SQL("select * from foo").as(SqlParser.str("foo").*)
    }
  }

  // keep comments

  def fetchOne: Future[Option[String]] = {
    _wsClient.url("http://127.0.0.1:9000")
      .get()
      .map {
        case r if r.status == 200 => Some(r.body)
        case _                    => None
      }
  }
  def _database: Database = Play.current.injector.instanceOf[Database]
  def _wsClient: WSClient = Play.current.injector.instanceOf[WSClient]
  // keep comments

}
@deprecated("(scalafix) Migrate to DI", "2.3.0")
object FooDAO_03 {
  // FIXME(scalafix): Remove once migration is completed ("services.FooDAO_03")
  lazy val _instance: FooDAO_03 = Play.current.injector.instanceOf[FooDAO_03]
  implicit def _instance(f: FooDAO_03.type): FooDAO_03 = f._instance
}
