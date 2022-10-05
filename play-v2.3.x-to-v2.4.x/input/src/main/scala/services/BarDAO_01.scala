/*
rule = MigrateInjectAll
 */
// format: off
package services

import play.api.Play.current
import play.api.cache.Cache

object BarDAO_01 {
  def fetchAll: List[String] = {
    Cache.getOrElse("foo") {
      FooDAO_01.fetchAll
    }
  }
}
