package play.fix

import scalafix.v1._

import scala.meta._

final class MigrateJson() extends SemanticRule("MigrateJson") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    object JsValue {
      val sym: Symbol = Symbols.fromFQCN("play.api.libs.json.JsValue")
      def unapply(t: Term): Option[Term] = {
        val s1 = t.symbol
        val p1 = s1.getParents
        if (s1 == sym || p1.contains(sym)) {
          Some(t)
        } else {
          val s2 = s1.owner
          val p2 = s2.getParents
          if (s2 == sym || p2.contains(sym)) {
            Some(t)
          } else {
            s1.info.map(_.signature).flatMap {
              case MethodSignature(_, _, TypeRef(_, s, _)) if s.value.startsWith("play/api/libs/json/") =>
                Some(t)
              case _ =>
                None
            }
          }
        }
      }
    }

    doc.tree
      .collect {
        case t @ Term.ApplyType(Term.Select(js, Term.Name("as")), List(Type.Apply(Type.Name("Option"), List(tpe)))) =>
          Patch.replaceTree(t, s"$js.asOpt[$tpe]")
      }
      .asPatch
      .atomic
  }
}
