package play.fix

import scalafix.v1._

object Symbols {
  def fromFQCN(s: String): Symbol =
    Symbol(s.replace('.', '/') + "#")
}
