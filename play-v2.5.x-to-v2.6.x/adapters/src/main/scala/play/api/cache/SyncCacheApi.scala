package play.api.cache

import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

trait SyncCacheApi extends CacheApi {
  def getOrElseUpdate[A: ClassTag](key: String, expiration: Duration = Duration.Inf)(orElse: => A): A
}
