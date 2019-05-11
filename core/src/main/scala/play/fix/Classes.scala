package play.fix

import scala.meta._

object Classes {

  implicit class ClassesExt(val c: Defn.Class) extends AnyVal {

    def isCase: Boolean = Mods.isCase(c.mods)

    def rename(name: String): Defn.Class = c.copy(
      name = Type.Name(name)
    )

    def mapParam(fn: List[List[Term.Param]] => List[List[Term.Param]]): Defn.Class = c.copy(
      ctor = c.ctor.copy(
        paramss = fn(c.ctor.paramss)
      )
    )

    def ensureParam(p: Term.Param): Defn.Class =
      if (!hasParam(p)) {
        addParam(p)
      } else {
        c
      }

    def hasParam(p: Term.Param): Boolean = {
      c.ctor.paramss.exists(_.exists {
        case Term.Param(_, name, _, _) if name == p.name => true
        case _                                           => false
      })
    }

    def addParam(p: Term.Param): Defn.Class = {
      val isImplicit = Mods.isImplicit(p.mods)
      mapParam {
        case Nil =>
          List(List(p))
        case g :: Nil =>
          val isImplicitGroup = Mods.isImplicitParamList(g)
          (isImplicit, isImplicitGroup) match {
            case (true, true)   => List(g :+ p)
            case (true, false)  => List(g, List(p))
            case (false, true)  => List(List(p), g)
            case (false, false) => List(g :+ p)
          }
        case l if isImplicit =>
          val idx = l.indexWhere(Mods.isImplicitParamList)
          if (idx >= 0) {
            l.updated(idx, l(idx) :+ p)
          } else {
            l :+ List(p)
          }
        case l =>
          val idx = l.indexWhere(lp => !Mods.isImplicitParamList(lp))
          if (idx >= 0) {
            l.updated(idx, l(idx) :+ p)
          } else {
            List(p) +: l
          }
      }
    }

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
