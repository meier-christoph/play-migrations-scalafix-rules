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

trait FooDAO_03 {

  // keep comments

  /** keep doc */
  def fetchAll: List[String] = {
    DB.withConnection { implicit conn =>
      SQL("select * from foo").as(SqlParser.str("foo").*)
    }
  }

  // keep comments

  def fetchOne: Future[Option[String]] = {
    WS.url("http://127.0.0.1:9000")
      .get()
      .map {
        case r if r.status == 200 => Some(r.body)
        case _                    => None
      }
  }

  // keep comments

}
object FooDAO_03 extends FooDAO_03
