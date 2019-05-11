package play.fix

import scala.meta._

object Mods {

  def isCase(mods: List[Mod]): Boolean =
    mods.exists {
      case Mod.Case() => true
      case _          => false
    }

  def isImplicit(mods: List[Mod]): Boolean =
    mods.exists {
      case Mod.Implicit() => true
      case _              => false
    }

  def isImplicitParamList(params: List[Term.Param]): Boolean =
    params.headOption.map(_.mods).exists {
      case Mod.Implicit() => true
      case _              => false
    }

}
