package play.fix

import scalafix.v1._

import scala.meta._
import scala.meta.transversers.SimpleTraverser

final class MigrateLogger() extends SemanticRule("MigrateLogger") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    object Logger {
      def unapply(t: Term): Option[Term] = {
        if (t.symbol.value == "play/api/Logger.") {
          Some(t)
        } else None
      }
    }

    val imports = ImportHolder(doc.tree)
    val logging = Init(Type.Name("Logging"), Name.Anonymous(), Nil)

    def replaceLogger(tree: Tree): List[Patch] = {
      tree.collect {
        case t @ Logger(_) =>
          Patch.replaceTree(t, "logger")
      }
    }

    val buf = scala.collection.mutable.ListBuffer[Patch]()
    object traverser extends SimpleTraverser {
      override def apply(tree: Tree): Unit = {
        tree match {
          case t @ Defn.Class(_, _, _, _, _) =>
            val internal = replaceLogger(t)
            if (internal.nonEmpty) {
              buf += internal.asPatch
              imports.ensure(importer"play.api.Logging")
              val fixed = t.ensureType(logging)
              buf += ExtraPatch.replaceClassDef(t, fixed)
            }
          case t @ Defn.Trait(_, _, _, _, _) =>
            val internal = replaceLogger(t)
            if (internal.nonEmpty) {
              buf += internal.asPatch
              imports.ensure(importer"play.api.Logging")
              val fixed = t.ensureType(logging)
              buf += ExtraPatch.replaceTraitDef(t, fixed)
            }
          case t @ Defn.Object(_, _, _) =>
            val internal = replaceLogger(t)
            if (internal.nonEmpty) {
              buf += internal.asPatch
              imports.ensure(importer"play.api.Logging")
              val fixed = t.ensureType(logging)
              buf += ExtraPatch.replaceObjectDef(t, fixed)
            }
          case _ =>
            super.apply(tree)
        }
      }
    }
    traverser(doc.tree)
    buf.toList.asPatch.atomic + imports.asPatch
  }
}
