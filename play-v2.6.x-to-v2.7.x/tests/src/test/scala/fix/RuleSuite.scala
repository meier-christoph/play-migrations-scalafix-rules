package fix

import org.scalatest.FunSuiteLike
import scalafix.testkit.{AbstractSemanticRuleSuite, DiffAssertions}

class RuleSuite extends AbstractSemanticRuleSuite with FunSuiteLike {
  runAllTests()

  override def compareContents(original: String, revised: String): String = {
    RuleSuite.compareContents(original, revised)
  }
}
object RuleSuite {
  def compareContents(original: String, revised: String): String = {
    def splitLines(s: String): Array[String] =
      s.trim
        .replace("\r", "")
        .split("\n")
        .map(_.trim)
        .filter(_.nonEmpty)
    DiffAssertions.compareContents(splitLines(original), splitLines(revised))
  }
}
