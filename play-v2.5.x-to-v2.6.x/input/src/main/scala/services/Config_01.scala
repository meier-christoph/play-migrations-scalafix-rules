/*
rule = MigrateConfiguration
 */
// format: off
package services

import javax.inject.Inject
import play.api.Configuration

class Config_01 @Inject()(conf: Configuration) {

  val s01: Option[String] = conf.getString("foo.bar")
  val s02: String = conf.getString("foo.bar").getOrElse("default")
  val s03: Seq[String] = conf.getStringSeq("foo.bar").getOrElse(Nil)
  val s04: String = conf.underlying.getString("foo.bar")

  val b01: Option[Boolean] = conf.getBoolean("foo.bar")
  val b02: Boolean = conf.getBoolean("foo.bar").getOrElse(true)
  val b03: Seq[Boolean] = conf.getBooleanSeq("foo.bar").map(_.map(_.booleanValue())).getOrElse(Nil)
  val b04: Boolean = conf.underlying.getBoolean("foo.bar")
  
  val i01: Option[Int] = conf.getInt("foo.bar")
  val i02: Int = conf.getInt("foo.bar").getOrElse(123)
  val i03: Seq[Int] = conf.getIntSeq("foo.bar").map(_.map(_.intValue())).getOrElse(List(1,2,3))
  val i04: Int = conf.underlying.getInt("foo.bar")
  
  val l01: Option[Long] = conf.getLong("foo.bar")
  val l02: Long = conf.getLong("foo.bar").getOrElse(123L)
  val l03: Seq[Long] = conf.getLongSeq("foo.bar").map(_.map(_.longValue())).getOrElse(Nil)
  val l04: Long = conf.underlying.getLong("foo.bar")
  
  val d01: Option[Double] = conf.getDouble("foo.bar")
  val d02: Double = conf.getDouble("foo.bar").getOrElse(1.2)
  val d03: Seq[Double] = conf.getDoubleSeq("foo.bar").map(_.map(_.doubleValue())).getOrElse(Nil)
  val d04: Double = conf.underlying.getDouble("foo.bar")
  
  val n01: Option[Number] = conf.getNumber("foo.bar")
  val n02: Number = conf.getNumber("foo.bar").getOrElse(123)
  val n03: Seq[Number] = conf.getNumberSeq("foo.bar").getOrElse(Nil)
  val n04: Number = conf.underlying.getNumber("foo.bar")
}
