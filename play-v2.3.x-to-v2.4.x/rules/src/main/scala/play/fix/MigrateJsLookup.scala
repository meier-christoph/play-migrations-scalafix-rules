package play.fix

import metaconfig.Configured
import scalafix.v1._

import scala.meta._
import scala.meta.transversers.SimpleTraverser

final class MigrateJsLookup(config: MigrateJsLookupConfig) extends SemanticRule("MigrateJsLookup") {
  def this() = this(MigrateJsLookupConfig.default)

  override def withConfiguration(config: Configuration): Configured[Rule] = {
    config.conf
      .getOrElse("MigrateJsLookup")(this.config)
      .map(newConfig => new MigrateJsLookup(newConfig))
  }

  override def fix(implicit doc: SemanticDocument): Patch = {
    val imports = ImportHolder(doc.tree)

    object JsLookup {
      val sym: Symbol = Symbols.fromFQCN("play.api.libs.json.JsValue")
      def unapply(t: Term): Option[Term] = {
        val s = t.symbol.owner
        val p = s.getParents
        if (s == sym || p.contains(sym)) Some(t) else None
      }
    }

    val allowed = List("as", "asOpt", "validate", "transform") ++ config.allowed
    val buf = scala.collection.mutable.ListBuffer[Patch]()
    object traverser extends SimpleTraverser {
      override def apply(tree: Tree): Unit = {
        tree match {
          case Term.ApplyType(Term.Select(JsLookup(Term.ApplyInfix(_, Term.Name("\\"), _, _)), Term.Name(n)), _) if allowed.contains(n) =>
            ()
          case Term.Apply(Term.Select(JsLookup(Term.ApplyInfix(_, Term.Name("\\"), _, _)), Term.Name(n)), _) if allowed.contains(n) =>
            ()
          case Term.Select(JsLookup(Term.ApplyInfix(_, Term.Name("\\"), _, _)), Term.Name(n)) if allowed.contains(n) =>
            ()
          case t @ JsLookup(Term.ApplyInfix(_, Term.Name("\\"), _, _)) =>
            imports.ensure(importer"play.api.libs.json.JsNull")
            buf += Patch.replaceTree(t, s"($t).getOrElse(JsNull)")
          case _ =>
            super.apply(tree)
        }
      }
    }
    traverser(doc.tree)
    buf.toList.asPatch.atomic + imports.asPatch
  }
}
