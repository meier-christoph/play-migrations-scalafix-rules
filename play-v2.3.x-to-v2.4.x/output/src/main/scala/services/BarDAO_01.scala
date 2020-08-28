// format: off
package services

import play.api.Play.current
import play.api.cache.Cache
import javax.inject.Inject
import play.api.Play
import play.api.cache.CacheApi

class BarDAO_01 @Inject() (_cache: CacheApi) {
  def fetchAll: List[String] = {
    _cache.getOrElse("foo") {
      FooDAO_01.fetchAll
    }
  }
}
object BarDAO_01 {
  // FIXME(scalafix): Remove once migration is completed ("services.BarDAO_01")
  lazy val _instance: BarDAO_01 = Play.current.injector.instanceOf[BarDAO_01]
  implicit def _instance(f: BarDAO_01.type): BarDAO_01 = f._instance
}
