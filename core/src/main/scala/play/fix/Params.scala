package play.fix

import scala.meta._

class ParamGroup(val l: List[Term.Param]) {
  lazy val isImplicit: Boolean = l.headOption.exists(_.mods.nonEmpty)
  def toScalaCode: String = l match {
    case Nil =>
      ""
    case _ if isImplicit =>
      l.mkString("implicit ", ", ", "")
    case _ =>
      l.mkString(", ")
  }
}

class Params(val l: List[ParamGroup]) {
  def addParam(name: String, className: String, isVal: Boolean = false): Params = {
    val mods = if (isVal) List(Mod.ValParam()) else List()
    val p = Term.Param(mods, Name(name), Some(Type.Name(className)), None)
    new Params(l match {
      case other         => other
      case Nil           => List(new ParamGroup(List(p)), new ParamGroup(Nil))
      case i :: Nil      => List(new ParamGroup(List(p)), i)
      case r :: i :: Nil => List(new ParamGroup(r.l :+ p), i)
//      case g :: t if g.isImplicit => new ParamGroup(List(p)) :: g :: t
//      case g :: t                 => new ParamGroup(g.l :+ p) :: t
    })
  }

//  def addImplicitParam(name: String, className: String, isVal: Boolean = false): Params = {
//    val mods = Mod.Implicit() :: (if (isVal) List(Mod.ValParam()) else Nil)
//    val p = Term.Param(mods, Name(name), Some(Type.Name(className)), None)
//    new Params(l match {
//      case Nil => List(new ParamGroup(List(p)))
//      case nel if nel.exists(_.isImplicit) =>
//        nel.map {
//          case g if g.isImplicit => new ParamGroup(g.l :+ p)
//          case g                 => g
//        }
//      case nel => nel :+ new ParamGroup(List(p))
//    })
//  }

  def toScalaCode: String = l.filter(_.l.nonEmpty) match {
    case Nil => ""
    case nel => nel.map(_.toScalaCode).mkString("(", ")(", ")")
  }
}
object Params {
  def apply(l: List[List[Term.Param]]): Params = new Params(l.map(new ParamGroup(_)))
}
