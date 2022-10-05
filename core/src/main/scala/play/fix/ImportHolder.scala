package play.fix

import scalafix.patch.Patch

import scala.meta._

final class ImportHolder {
  private var patch: Patch = Patch.empty
  private var buffer: Map[String, Importer] = Map.empty

  def register(g: Importer): Unit = {
    val p = g.ref.syntax
    buffer = buffer.get(p) match {
      case Some(i) => buffer.updated(p, i.copy(importees = i.importees ++ g.importees))
      case None    => buffer.updated(p, g)
    }
  }

  def hasImport(i: Importer): Boolean = {
    if (i.importees.length != 1) {
      throw new IllegalArgumentException("require importer with exactly one importee")
    }
    buffer.get(i.ref.syntax) match {
      case Some(l) =>
        l.importees.exists {
          case Importee.Wildcard()                                     => true
          case Importee.Name(n) if n.syntax == i.importees.head.syntax => true
          case _                                                       => false
        }
      case _ => false
    }
  }

  def ensure(i: Importer): ImportHolder = {
    if (!hasImport(i)) {
      register(i)
      patch = patch + Patch.addGlobalImport(i)
    }
    this
  }

  def ensureNot(i: Importer): ImportHolder = {
    if (hasImport(i)) {
      val p = i.ref.syntax
      buffer.get(p).foreach { b =>
        val c = i.importees.head.syntax
        b.importees
          .find {
            case Importee.Name(n) if n.syntax == c        => true
            case n @ Importee.Wildcard() if n.syntax == c => true
            case _                                        => false
          }
          .foreach { imp =>
            patch = patch + Patch.removeImportee(imp)
            buffer = buffer.updated(p, b.copy(importees = b.importees.filter(_.syntax == c)))
          }
      }
    }
    this
  }

  def asPatch: Patch =
    patch
}
object ImportHolder {
  def apply(tree: Tree): ImportHolder = {
    val buff = new ImportHolder()
    tree.traverse {
      case i: Importer => buff.register(i)
    }
    buff
  }
}
