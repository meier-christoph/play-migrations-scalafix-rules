/*
 */
import play.api.libs.json.JsValue

package object services {
  implicit class JsonOps(val js: JsValue) extends AnyVal {
    def required: JsValue = js
  }
}
