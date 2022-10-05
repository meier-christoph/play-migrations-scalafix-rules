/*
rule = MigrateJson
 */
// format: off
package services

import play.api.libs.json.{JsObject, Json}

object Json_02 {
  val js: JsObject = Json.obj()
  val s1: Option[String] = js.as[Option[String]]
  val s2: Option[String] = (js \ "foo").as[Option[String]]
  val s3: Option[String] = (js \ "foo" \ "bar").as[Option[String]]
}
