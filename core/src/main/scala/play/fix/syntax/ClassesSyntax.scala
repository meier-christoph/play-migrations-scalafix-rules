package play.fix.syntax

import play.fix.Mods
import play.fix.syntax.ClassesSyntax._

import scala.meta._

trait ClassesSyntax {
  implicit final def fixClassOps(c: Defn.Class): ClassOps =
    new ClassOps(c)
}
object ClassesSyntax {
  class ClassOps(val c: Defn.Class) extends AnyVal {

    def debug(): Unit =
      println(c.structure)

    def isCase: Boolean =
      Mods.isCase(c.mods)

    def rename(name: String): Defn.Class =
      c.copy(
        name = Type.Name(name)
      )

    // modifiers (e.g. case, abstract, ...) and annotations

    def mapMods(fn: List[Mod] => List[Mod]): Defn.Class =
      c.copy(mods = fn(c.mods))

    def ensureMod(m: Mod): Defn.Class =
      if (!hasMod(m)) addMod(m) else c
    def hasMod(m: Mod): Boolean =
      Mods.contains(c.mods, m)
    def addMod(m: Mod): Defn.Class =
      mapMods(_ :+ m)

    def ensureAnnot(a: Mod.Annot): Defn.Class =
      c.copy(
        ctor = c.ctor.copy(
          mods = if (Mods.contains(c.ctor.mods, a)) {
            c.ctor.mods
          } else {
            c.ctor.mods :+ a
          }
        )
      )

    // params

    def mapParam(fn: List[List[Term.Param]] => List[List[Term.Param]]): Defn.Class =
      c.copy(
        ctor = c.ctor.copy(
          paramss = fn(c.ctor.paramss)
        )
      )

    def ensureParam(p: Term.Param): Defn.Class =
      if (!hasParam(p)) addParam(p) else c

    def ensureNotParam(p: Term.Param): Defn.Class =
      if (hasParam(p)) removeParam(p) else c

    def hasParam(p: Term.Param): Boolean =
      c.ctor.paramss.exists(_.exists {
        case Term.Param(_, name, _, _) if name.syntax == p.name.syntax =>
          true
        case _ =>
          false
      })

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

    def removeParam(p: Term.Param): Defn.Class =
      removeParamByName(name = Some(p.name.syntax), className = p.decltpe.map(_.syntax))

    def removeParamByName(name: Option[String] = None, className: Option[String] = None): Defn.Class = {
      mapParam {
        case Nil => Nil
        case l =>
          l.map { lp =>
            lp.flatMap {
              case Term.Param(_, n, _, _) if name.contains(n.syntax) =>
                None
              case Term.Param(_, _, Some(tpe), _) if className.contains(tpe.syntax) =>
                None
              case tpe =>
                Some(tpe)
            }
          }
      }
    }

    // types (e.g. extends and with)

    def mapInit(fn: List[Init] => List[Init]): Defn.Class =
      c.copy(
        templ = c.templ.copy(
          inits = fn(c.templ.inits)
        )
      )

    def ensureType(i: Init): Defn.Class =
      if (!hasType(i)) addType(i) else c

    def ensureNotType(i: Init): Defn.Class =
      if (hasType(i)) removeType(i) else c

    def hasType(i: Init): Boolean =
      c.templ.inits.exists {
        case Init(tpe, _, _) if tpe == i.tpe => true
        case _                               => false
      }

    def addType(i: Init): Defn.Class = mapInit(_ :+ i)
    def removeType(i: Init): Defn.Class = mapInit(_.filterNot(_.tpe == i.tpe))

    // body

    def mapBody(fn: List[Stat] => List[Stat]): Defn.Class =
      c.copy(
        templ = c.templ.copy(
          stats = fn(c.templ.stats)
        )
      )
    def ignoreBody: Defn.Class = mapBody(_ => Nil)
  }
}
