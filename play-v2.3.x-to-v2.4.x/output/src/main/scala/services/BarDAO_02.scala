// format: off
package services

import play.api.Play.current
import play.api.cache.Cache
import javax.inject.Inject

class BarDAO_02 @Inject() (_FooDAO_01: FooDAO_01) {
  def fetchAll: List[String] = {
    Cache.getOrElse("foo") {
      _FooDAO_01.fetchAll
    }
  }
}
