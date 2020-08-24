package play.fix.syntax

import play.fix.syntax.ObjectSyntax._

import scala.meta._

trait ObjectSyntax {
  implicit final def fixObjectOps(o: Defn.Object): ObjectOps =
    new ObjectOps(o)
}
object ObjectSyntax {
  class ObjectOps(val c: Defn.Object) extends AnyVal {

    def mapInit(fn: List[Init] => List[Init]): Defn.Object =
      c.copy(
        templ = c.templ.copy(
          inits = fn(c.templ.inits)
        )
      )

    def ensureType(i: Init): Defn.Object =
      if (!hasType(i)) addType(i) else c

    def ensureNotType(i: Init): Defn.Object =
      if (hasType(i)) removeType(i) else c

    def hasType(i: Init): Boolean =
      c.templ.inits.exists {
        case Init(tpe, _, _) if tpe == i.tpe => true
        case _                               => false
      }

    def addType(i: Init): Defn.Object = mapInit(_ :+ i)

    def removeType(i: Init): Defn.Object = mapInit(_.filterNot(_.tpe == i.tpe))

    def mapSelf(fn: Self => Self): Defn.Object =
      c.copy(
        templ = c.templ.copy(
          self = fn(c.templ.self)
        )
      )

    def mapBody(fn: List[Stat] => List[Stat]): Defn.Object =
      c.copy(
        templ = c.templ.copy(
          stats = fn(c.templ.stats)
        )
      )

    def ignoreBody: Defn.Object = mapBody(_ => Nil)
  }
}
