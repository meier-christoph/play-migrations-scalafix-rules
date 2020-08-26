/*
rule = MigrateInjectAll
 */
// format: off
package services

import anorm._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WS

import scala.concurrent.Future

object FooDAO_01 {
  def fetchAll: List[String] = {
    DB.withConnection { implicit conn =>
      SQL("select * from foo").as(SqlParser.str("foo").*)
    }
  }

  def fetchOne: Future[Option[String]] = {
    WS.url("http://127.0.0.1:9000")
      .get()
      .map {
        case r if r.status == 200 => Some(r.body)
        case _                    => None
      }
  }
}
