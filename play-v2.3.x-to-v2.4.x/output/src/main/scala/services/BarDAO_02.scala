// format: off
package services

import javax.inject.Inject
import play.api.cache.CacheApi

class BarDAO_02 @Inject() (_cache: CacheApi, _FooDAO_01: FooDAO_01) {
  def fetchAll: List[String] = {
    _cache.getOrElse("foo") {
      _FooDAO_01.fetchAll
    }
  }
}
