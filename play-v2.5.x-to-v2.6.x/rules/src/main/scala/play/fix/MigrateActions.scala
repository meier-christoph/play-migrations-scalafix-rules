package play.fix

import play.fix.Symbols._
import scalafix.v1._

import scala.meta._

final class MigrateActions() extends SemanticRule("MigrateActions") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    object Action {
      def unapply(t: Term): Boolean = {
        val symbols = t.getParents
        symbols.map(_.value).contains("play/api/mvc/ActionBuilder#")
      }
    }

    def patchActionWithImplicitRequestArg(b: Term.Block): Patch =
      b.stats.headOption match {
        case Some(fn: Term.Function) =>
          fn match {
            case Term.Function(List(Term.Param(Nil, n @ Term.Name(_), _, _)), _) =>
              Patch.addLeft(n, "implicit ")
            case Term.Function(List(Term.Param(Nil, n @ Name.Anonymous(), _, _)), _) =>
              Patch.addLeft(n, "implicit _request") + Patch.removeTokens(n.tokens)
            case _ =>
              Patch.empty
          }
        case _ =>
          Patch.addRight(b.tokens.head, " implicit _request =>")
      }

    doc.tree
      .collect {
        case Term.Apply(Term.Apply(Term.Select(Action(), _), _), List(b: Term.Block)) =>
          patchActionWithImplicitRequestArg(b)
        case Term.Apply(Term.Apply(Action(), _), List(b: Term.Block)) =>
          patchActionWithImplicitRequestArg(b)
        case Term.Apply(Term.Select(Action(), _), List(b: Term.Block)) =>
          patchActionWithImplicitRequestArg(b)
        case Term.Apply(Action(), List(b: Term.Block)) =>
          patchActionWithImplicitRequestArg(b)
      }
      .asPatch
      .atomic
  }
}
