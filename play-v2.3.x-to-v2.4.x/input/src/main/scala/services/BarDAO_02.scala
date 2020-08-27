/*
rule = MigrateInjectAll
MigrateInjectAll.types = [
  "services.FooDAO_01"
]
 */
// format: off
package services

import javax.inject.Inject
import play.api.cache.CacheApi

class BarDAO_02 @Inject() (_cache: CacheApi) {
  def fetchAll: List[String] = {
    _cache.getOrElse("foo") {
      FooDAO_01.fetchAll
    }
  }
}
