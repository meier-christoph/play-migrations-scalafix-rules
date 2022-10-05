/*
rule = MigrateJsLookup
 */
// format: off
package services

import play.api.libs.json.{JsArray, JsObject, Json, OFormat}

class JsLookup_01 {
  case class Bar(f: String)
  object Bar {
    implicit val f: OFormat[Bar] = Json.format[Bar]
  }

  val arr: JsArray = Json.arr(Json.obj("bar" -> Json.obj("f" -> "foo")))
  val obj: JsObject = arr(0).as[JsObject]
  val foo: Bar = (arr(0) \ "bar").as[Bar]
  val faa: Bar = ((arr \ 0) \ "bar").as[Bar]
  val fuu: Bar = (arr \ 0 \ "bar").as[Bar]
  val bar: Bar = (arr.apply(0) \ "bar").as[Bar]
  val baz: Bar = ((arr \ 0 \ "bar").as[JsArray].apply(0) \ "bar").as[Bar]
}
