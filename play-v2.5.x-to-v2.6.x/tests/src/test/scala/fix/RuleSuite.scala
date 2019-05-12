package fix

import java.nio.file.Paths

import scalafix.testkit.{DiffAssertions, RuleTest, SemanticRuleSuite}

class RuleSuite extends SemanticRuleSuite() {
  def startsWith(t: RuleTest, p: String): Boolean = t.path.testPath.syntax.startsWith(p)
  def contains(t: RuleTest, p: String): Boolean = t.path.testPath.syntax.contains(p)
  def endsWith(t: RuleTest, p: String): Boolean = t.path.testPath.syntax.endsWith(s"$p.scala")
  def byPath(t: RuleTest, p: String): Boolean = t.path.testPath.toNIO == Paths.get(p)

  testsToRun
//    .filter(contains(_, "example"))
//    .filter(endsWith(_, "I18nSupportCtrl_01"))
    .foreach(runOn)

  override def compareContents(original: String, revised: String): String = {
//    println("was: \n" + original)
//    println("expected: \n" + revised)
    RuleSuite.compareContents(original, revised)
  }

}
object RuleSuite {
  def compareContents(original: String, revised: String): String = {
    def splitLines(s: String): Array[String] =
      s.trim
        .replaceAllLiterally("\r\n|\r|\n", "\n")
        .split("\n")
        .map(_.trim)
        .filter(_.nonEmpty)
    DiffAssertions.compareContents(splitLines(original), splitLines(revised))
  }
}
