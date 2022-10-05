// format: off
package services

import anorm._
import play.api.db.Database

import javax.inject.Inject

/** The ExecutionContext should be provided using DI. */
class AnormDAO_01 @Inject()(database: Database) {

  // keep comments

  /** keep doc */
  def addCity(): Unit = {
    database.withConnection { implicit c =>
      SQL("insert into City(name, country) values ({name}, {country})")
        .on("name" -> "Cambridge", "country" -> "New Zealand")
        .executeInsert()
    }
  }

  // keep comments
}

// keep comments
