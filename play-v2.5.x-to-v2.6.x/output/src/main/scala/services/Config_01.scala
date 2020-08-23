// format: off
package services

import javax.inject.Inject
import play.api.Configuration

class Config_01 @Inject()(conf: Configuration) {

  val s01: Option[String] = conf.getOptional[String]("foo.bar")
  val s02: String = conf.getOptional[String]("foo.bar").getOrElse("default")
  val s03: Seq[String] = conf.getOptional[Seq[String]]("foo.bar").getOrElse(Nil)
  val s04: String = conf.get[String]("foo.bar")
  
  val b01: Option[Boolean] = conf.getOptional[Boolean]("foo.bar")
  val b02: Boolean = conf.getOptional[Boolean]("foo.bar").getOrElse(true)
  val b03: Seq[Boolean] = conf.getOptional[Seq[Boolean]]("foo.bar").getOrElse(Nil)
  val b04: Boolean = conf.get[Boolean]("foo.bar")
  
  val i01: Option[Int] = conf.getOptional[Int]("foo.bar")
  val i02: Int = conf.getOptional[Int]("foo.bar").getOrElse(123)
  val i03: Seq[Int] = conf.getOptional[Seq[Int]]("foo.bar").getOrElse(List(1,2,3))
  val i04: Int = conf.get[Int]("foo.bar")
  
  val l01: Option[Long] = conf.getOptional[Long]("foo.bar")
  val l02: Long = conf.getOptional[Long]("foo.bar").getOrElse(123L)
  val l03: Seq[Long] = conf.getOptional[Seq[Long]]("foo.bar").getOrElse(Nil)
  val l04: Long = conf.get[Long]("foo.bar")
  
  val d01: Option[Double] = conf.getOptional[Double]("foo.bar")
  val d02: Double = conf.getOptional[Double]("foo.bar").getOrElse(1.2)
  val d03: Seq[Double] = conf.getOptional[Seq[Double]]("foo.bar").getOrElse(Nil)
  val d04: Double = conf.get[Double]("foo.bar")
  
  val n01: Option[Number] = conf.getOptional[Number]("foo.bar")
  val n02: Number = conf.getOptional[Number]("foo.bar").getOrElse(123)
  val n03: Seq[Number] = conf.getOptional[Seq[Number]]("foo.bar").getOrElse(Nil)
  val n04: Number = conf.get[Number]("foo.bar")
}
