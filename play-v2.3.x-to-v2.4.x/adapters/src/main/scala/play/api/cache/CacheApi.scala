package play.api.cache

import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

trait CacheApi {
  def set(key: String, value: Any, expiration: Duration = Duration.Inf)
  def remove(key: String)
  def getOrElse[A: ClassTag](key: String, expiration: Duration = Duration.Inf)(orElse: => A): A
  def get[T: ClassTag](key: String): Option[T]
}
