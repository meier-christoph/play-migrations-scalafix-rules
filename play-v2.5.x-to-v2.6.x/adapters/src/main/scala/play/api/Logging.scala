package play.api

trait Logging {
  protected val logger: Logger = Logger(getClass)
}
