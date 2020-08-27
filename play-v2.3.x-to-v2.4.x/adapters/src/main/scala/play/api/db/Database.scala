package play.api.db

import java.sql.Connection

import javax.sql.DataSource

trait Database {
  def name: String
  def dataSource: DataSource
  def url: String
  def getConnection(): Connection
  def getConnection(autocommit: Boolean): Connection
  def withConnection[A](block: Connection => A): A
  def withConnection[A](autocommit: Boolean)(block: Connection => A): A
  def withTransaction[A](block: Connection => A): A
  def shutdown(): Unit
}
