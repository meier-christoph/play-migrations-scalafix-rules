package play.fix

import scalafix.v1._

import scala.meta._

object Symbols {

  private def getParentSymbols(symbol: Symbol)(implicit doc: Symtab): Set[Symbol] = {
    symbol.info.map(_.signature) match {
      case Some(ClassSignature(_, parents, _, _)) =>
        Set(symbol) ++ parents.flatMap {
          case TypeRef(_, symbol, _) =>
            getParentSymbols(symbol)
          case _ =>
            Set.empty[Symbol]
        }
      case _ =>
        Set.empty[Symbol]
    }
  }

  def fromFQCN(s: String): Symbol = {
    Symbol(s.replace('.', '/') + "#")
  }

  implicit class TermOps(val t: Term) extends AnyVal {
    def getParents(implicit doc: SemanticDocument): Set[Symbol] = {
      getParentSymbols(t.symbol)
    }
  }

  implicit class SymbolOps(val s: Symbol) extends AnyVal {
    def getParents(implicit doc: SemanticDocument): Set[Symbol] = {
      getParentSymbols(s)
    }
    def toType: Type.Name =
      Type.Name(s.displayName)
    def toImporter: Importer = {
      val t :: pkg = s.value.stripSuffix("#").split('/').reverse.toList
      def toPkg(l: List[String]): Term.Ref =
        l match {
          case Nil       => Term.Name("_root_")
          case p :: Nil  => Term.Name(p)
          case p :: tail => Term.Select(toPkg(tail), Term.Name(p))
        }
      Importer(
        toPkg(pkg),
        List(Importee.Name(Name(t)))
      )
    }
  }
}
