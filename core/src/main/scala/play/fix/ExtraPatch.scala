package play.fix

import play.fix.Classes._
import scalafix.patch.Patch

import scala.meta._
import scala.meta.tokens.Token.LeftBrace

object ExtraPatch {

  /**
    * Create a Patch to remove the entire class definition leaving only a block with the body.
    */
  def clearClassDef(c: Defn.Class)(implicit dialect: Dialect): Patch = {
    val toRm = c.tokens.takeWhile {
      case LeftBrace() => false
      case _           => true
    }
    toRm.foldLeft(Patch.empty)(_ + Patch.removeToken(_)).atomic
  }

  /**
    * Create a Patch to replace the entire class definition but preserving the body of the class.
    *
    * Note: scala meta only parses scala code i.e. if tou would write the whole class using
    * syntax you would loose all the comments and documentation.
    * Instead you are supposed to work with Patches to preserve the raw tokens, including docs
    * and comments.
    * It turned out to be a bit harder than I had anticipated so instead of writing all the
    * Patch statements using addLeft and addRight I am taking a different approach where
    * I delete the class definition first and then create a new one using scala meta
    * syntax.
    *
    */
  def replaceClassDef(from: Defn.Class, to: Defn.Class)(implicit dialect: Dialect): Patch = {
    val toAdd = to.ignoreBody.syntax // remove the body from this one as we will keep the original
    clearClassDef(from) + Patch.addLeft(from, toAdd + " ")
  }
}
