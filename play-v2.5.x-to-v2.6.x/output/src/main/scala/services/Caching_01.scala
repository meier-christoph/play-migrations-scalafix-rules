// format: off
package services

import javax.inject.Inject

import scala.concurrent.duration._
import play.api.cache.SyncCacheApi

class Caching_01 @Inject()(cache: SyncCacheApi) {
  val s01: Option[String] = cache.get[String]("foo.bar")
  val s02: String = cache.getOrElseUpdate[String]("foo.bar")("default")
  val s03: String = cache.getOrElseUpdate[String]("foo.bar", 5.seconds)("default")
  cache.remove("foo.bar")
  cache.set("foo.bar", "test")
  cache.set("foo.bar", "test", 5.seconds)
}
