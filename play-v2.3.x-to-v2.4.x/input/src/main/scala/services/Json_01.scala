/*
rule = MigrateJsLookup
MigrateJsLookup.allowed = ["required"]
 */
// format: off
package services

import play.api.libs.json.{JsObject, JsResult, JsSuccess, JsValue, Json, Reads}

object Json_01 {
  val r: Reads[JsValue] = Reads(js => JsSuccess(js))

  val js: JsObject = Json.obj()
  val s1: JsValue = js \ "foo"
  val t1: JsValue = js \ "foo" \ "bar"
  val s2: Option[String] = (js \ "foo").asOpt[String]
  val t2: Option[String] = (js \ "foo" \ "bar").asOpt[String]
  val s3: String = (js \ "foo").as[String]
  val t3: String = (js \ "foo" \ "bar").as[String]
  val s4: JsResult[String] = (js \ "foo").validate[String]
  val t4: JsResult[String] = (js \ "foo" \ "bar").validate[String]
  val s5: JsResult[JsValue] = (js \ "foo").transform(r)
  val t5: JsResult[JsValue] = (js \ "foo" \ "bar").transform(r)
  val s6: JsValue = (js \ "foo").required
  val t6: JsValue = (js \ "foo" \ "bar").required
}
