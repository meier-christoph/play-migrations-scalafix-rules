package play.fix

import scala.meta.Type

class TypeFinder(tpe: String) {
  def unapply(t: Type): Boolean = t match {
    case Type.Name(n) if n == tpe               => true
    case Type.With(_, Type.Name(n)) if n == tpe => true
    case Type.With(l, _)                        => unapply(l)
    case _                                      => false
  }
}
