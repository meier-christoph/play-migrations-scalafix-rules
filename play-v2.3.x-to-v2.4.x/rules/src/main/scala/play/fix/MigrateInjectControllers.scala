package play.fix

import scalafix.v1._

import scala.meta._

final class MigrateInjectControllers() extends SemanticRule("MigrateInjectControllers") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    object Controller {
      val sym: Symbol = Symbols.fromFQCN("play.api.mvc.Controller")

      def unapply(t: Term): Boolean =
        t.isInstanceOfType(sym)

      def unapply(l: List[Init]): Option[String] = {
        l.find {
          case Init(t, _, _) =>
            t.symbol.isInstanceOfType(sym)
          case _ =>
            false
        }.map(_.name.syntax)
      }
    }

    val inject = Mod.Annot(Init(Type.Name("Inject"), Name.Anonymous(), List(Nil)))

    val imports = ImportHolder(doc.tree)

    doc.tree
      .collect {
        case t @ Defn.Object(_, _, Template(_, Controller(_), _, _)) =>
          val fixed = t.toClass.ensureAnnot(inject)
          imports.ensure(importer"javax.inject.Inject")
          ExtraPatch.replaceObjectDef(t, fixed)
      }
      .asPatch
      .atomic +
      imports.asPatch
  }
}
