package play.fix

import scalafix.v1._

import scala.meta._

final class MigrateInjectControllers() extends SemanticRule("MigrateInjectControllers") {

  val inject: Mod.Annot = Mod.Annot(Init(Type.Name("Inject"), Name.Anonymous(), List(Nil)))

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

    val ignore = new scala.collection.mutable.HashSet[Symbol]()
    val imports = ImportHolder(doc.tree)
    doc.tree
      .collect {
        case t @ Defn.Trait(_, _, _, _, Template(_, Controller(_), _, _)) =>
          val fixed = t.toClass.ensureAnnot(inject)
          imports.ensure(importer"javax.inject.Inject")
          t.companion match {
            case Some(c @ Defn.Object(_, _, Template(_, _, _, Nil))) =>
              ignore += c.symbol
              ExtraPatch.replaceTraitDef(t, fixed) + Patch.removeTokens(c.tokens)
            case _ =>
              ExtraPatch.replaceTraitDef(t, fixed)
          }
        case t @ Defn.Object(_, _, Template(_, Controller(_), _, _)) if !ignore.contains(t.symbol) =>
          val fixed = t.toClass.ensureAnnot(inject)
          imports.ensure(importer"javax.inject.Inject")
          ExtraPatch.replaceObjectDef(t, fixed)
      }
      .asPatch
      .atomic +
      imports.asPatch
  }
}
