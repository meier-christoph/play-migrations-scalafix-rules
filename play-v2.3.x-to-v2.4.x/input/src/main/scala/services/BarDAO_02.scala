/*
rule = MigrateInjectAll
MigrateInjectAll.types = [
  "services.FooDAO_01"
]
 */
// format: off
package services

import play.api.Play.current
import play.api.cache.Cache

class BarDAO_02 {
  def fetchAll: List[String] = {
    Cache.getOrElse("foo") {
      FooDAO_01.fetchAll
    }
  }
}
