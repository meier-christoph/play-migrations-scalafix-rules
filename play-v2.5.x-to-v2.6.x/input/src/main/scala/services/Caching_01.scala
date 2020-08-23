/*
rule = MigrateCacheApi
 */
// format: off
package services

import javax.inject.Inject
import play.api.cache.CacheApi

import scala.concurrent.duration._

class Caching_01 @Inject()(cache: CacheApi) {
  val s01: Option[String] = cache.get[String]("foo.bar")
  val s02: String = cache.getOrElse[String]("foo.bar")("default")
  val s03: String = cache.getOrElse[String]("foo.bar", 5.seconds)("default")
  cache.remove("foo.bar")
  cache.set("foo.bar", "test")
  cache.set("foo.bar", "test", 5.seconds)
}
