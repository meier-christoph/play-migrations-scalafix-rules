package play.fix.syntax

import play.fix.Symbols
import play.fix.syntax.SymbolSyntax._
import scalafix.v1._

import scala.meta._

trait SymbolSyntax {
  implicit final def fixTermOps(t: Term): TermOps =
    new TermOps(t)
  implicit final def fixSymbolOps(s: Symbol): SymbolOps =
    new SymbolOps(s)
}
object SymbolSyntax {

  class TermOps(val term: Term) extends AnyVal {
    def getParents(implicit doc: SemanticDocument): Set[Symbol] = {
      new SymbolOps(term.symbol).getParents
    }
    def isInstanceOfType(s: Symbol)(implicit doc: SemanticDocument): Boolean = {
      new SymbolOps(term.symbol).isInstanceOfType(s)
    }
    def isInstanceOfType(s: String)(implicit doc: SemanticDocument): Boolean = {
      new SymbolOps(term.symbol).isInstanceOfType(s)
    }

    def isOfType(s: Symbol)(implicit doc: SemanticDocument): Boolean = {
      new SymbolOps(term.symbol).isOfType(s)
    }
    def isOfType(s: String)(implicit doc: SemanticDocument): Boolean = {
      new SymbolOps(term.symbol).isOfType(s)
    }
  }

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

  class SymbolOps(val sym: Symbol) extends AnyVal {
    def getParents(implicit doc: SemanticDocument): Set[Symbol] = {
      getParentSymbols(sym)
    }

    def isInstanceOfType(s: Symbol)(implicit doc: SemanticDocument): Boolean = {
      getParents.contains(s)
    }
    def isInstanceOfType(s: String)(implicit doc: SemanticDocument): Boolean = {
      val parent = Symbols.fromFQCN(s)
      isInstanceOfType(parent)
    }

    def isOfType(s: Symbol)(implicit doc: SemanticDocument): Boolean = {
      sym.owner == s
    }
    def isOfType(s: String)(implicit doc: SemanticDocument): Boolean = {
      val parent = Symbols.fromFQCN(s)
      isOfType(parent)
    }

    def toType: Type.Name =
      Type.Name(sym.displayName)

    def toFQCN: String =
      sym.value.replace('/', '.').stripSuffix("#").stripSuffix(".")

    def toImporter: Importer = {
      val t :: pkg = sym.value.stripSuffix("#").split('/').reverse.toList
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
