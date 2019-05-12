package play.fix

import scala.meta._

object Traits {

  implicit class TraitsExt(val c: Defn.Trait) extends AnyVal {

    def mapInit(fn: List[Init] => List[Init]): Defn.Trait = c.copy(
      templ = c.templ.copy(
        inits = fn(c.templ.inits)
      )
    )

    def ensureType(i: Init): Defn.Trait =
      if (!hasType(i)) addType(i) else c

    def ensureNotType(i: Init): Defn.Trait =
      if (hasType(i)) removeType(i) else c

    def hasType(i: Init): Boolean =
      c.templ.inits.exists {
        case Init(tpe, _, _) if tpe == i.tpe => true
        case _                               => false
      }

    def addType(i: Init): Defn.Trait = mapInit(_ :+ i)

    def removeType(i: Init): Defn.Trait = mapInit(_.filterNot(_.tpe == i.tpe))

    def mapSelf(fn: Self => Self): Defn.Trait = c.copy(
      templ = c.templ.copy(
        self = fn(c.templ.self)
      )
    )

    def mapBody(fn: List[Stat] => List[Stat]): Defn.Trait = c.copy(
      templ = c.templ.copy(
        stats = fn(c.templ.stats)
      )
    )

    def ignoreBody: Defn.Trait = mapBody(_ => Nil)
  }

}
