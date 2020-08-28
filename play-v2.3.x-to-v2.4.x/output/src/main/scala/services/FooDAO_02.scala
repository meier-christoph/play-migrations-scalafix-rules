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

trait FooDAO_02 {

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
  def _database: Database
  def _wsClient: WSClient
  // keep comments

}
object FooDAO_02 {
  // FIXME(scalafix): Remove once migration is completed ("services.FooDAO_02")
  lazy val _instance: FooDAO_02 = Play.current.injector.instanceOf[FooDAO_02]
  implicit def _instance(f: FooDAO_02.type): FooDAO_02 = f._instance
}
