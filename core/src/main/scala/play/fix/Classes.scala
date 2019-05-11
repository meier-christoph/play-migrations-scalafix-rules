package play.fix

import scala.meta._

object Classes {
  implicit class ClassesExt(val c: Defn.Class) extends AnyVal {
    def rename(name: String): Defn.Class = c.copy(
      name = Type.Name(name)
    )

    def mapParam(fn: List[List[Term.Param]] => List[List[Term.Param]]): Defn.Class = c.copy(
      ctor = c.ctor.copy(
        paramss = fn(c.ctor.paramss)
      )
    )

    def mapInit(fn: List[Init] => List[Init]): Defn.Class = c.copy(
      templ = c.templ.copy(
        inits = fn(c.templ.inits)
      )
    )

    def mapBody(fn: List[Stat] => List[Stat]): Defn.Class = c.copy(
      templ = c.templ.copy(
        stats = fn(c.templ.stats)
      )
    )

    def ignoreBody: Defn.Class = mapBody(_ => Nil)
  }
}
