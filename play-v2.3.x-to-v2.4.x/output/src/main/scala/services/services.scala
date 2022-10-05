import play.api.libs.json.{JsLookupResult, JsValue}

package object services {
  implicit class JsonOps(val js: JsLookupResult) extends AnyVal {
    def required: JsValue = js.get
  }
}
