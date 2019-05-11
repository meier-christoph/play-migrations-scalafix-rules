package play.fix

import scala.meta._

class Inits(l: List[Init]) {

  def addParentTypeFirst(className: String): Inits =
    Inits(Init(Type.Name(className), Name.Anonymous(), Nil) +: l)

  def addParentTypeLast(className: String): Inits =
    Inits(l :+ Init(Type.Name(className), Name.Anonymous(), Nil))

  def removeParentType(className: String): Inits =
    Inits(l.filterNot(_.tpe.syntax == className))

  def toScalaCode: String = l match {
    case Nil   => " "
    case other => other.mkString(" extends ", " with ", " ")
  }
}
object Inits {
  def apply(l: List[Init]): Inits = new Inits(l)
}
