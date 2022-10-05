package play.fix

import scalafix.v1._

import scala.meta._

final class MigrateConfiguration() extends SemanticRule("MigrateConfiguration") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    object Config {
      val sym: Symbol = Symbols.fromFQCN("play.api.Configuration")
      def unapply(t: Term): Option[Term] = {
        if (t.isOfType(sym)) {
          Some(t)
        } else None
      }
    }

    object JavaList {
      def unapply(t: Term): Option[(Term, String, Term, String)] =
        t match {
          case Term.Apply(
                Term.Select(
                  Config(Term.Apply(Term.Select(field, Term.Name(method)), List(key))),
                  Term.Name("map")
                ),
                List(
                  Term.Apply(
                    Term.Select(Term.Placeholder(), Term.Name("map")),
                    List(Term.Apply(Term.Select(Term.Placeholder(), Term.Name(asScala)), List()))
                  )
                )
              ) =>
            Some((field, method, key, asScala))
          case Term.Apply(
                Term.Select(
                  Config(Term.Apply(Term.Select(field, Term.Name(method)), List(key))),
                  Term.Name("map")
                ),
                List(
                  Term.AnonymousFunction(
                    Term.Apply(
                      Term.Select(Term.Placeholder(), Term.Name("map")),
                      List(Term.AnonymousFunction(Term.Apply(Term.Select(Term.Placeholder(), Term.Name(asScala)), List())))
                    )
                  )
                )
              ) =>
            Some((field, method, key, asScala))
          case _ => None
        }
    }

    doc.tree
      .collect {
        case t @ Config(Term.Apply(Term.Select(field, Term.Name("getString")), List(key))) =>
          Patch.replaceTree(t, s"$field.getOptional[String]($key)")
        case t @ Config(Term.Apply(Term.Select(field, Term.Name("getStringSeq")), List(key))) =>
          Patch.replaceTree(t, s"$field.getOptional[Seq[String]]($key)")
        case t @ Term.Apply(Term.Select(Config(Term.Select(field, Term.Name("underlying"))), Term.Name("getString")), List(key)) =>
          Patch.replaceTree(t, s"$field.get[String]($key)")

        case t @ Config(Term.Apply(Term.Select(field, Term.Name("getBoolean")), List(key))) =>
          Patch.replaceTree(t, s"$field.getOptional[Boolean]($key)")
        case t @ JavaList(field, "getBooleanSeq", key, "booleanValue") =>
          Patch.replaceTree(t, s"$field.getOptional[Seq[Boolean]]($key)")
        case t @ Term.Apply(Term.Select(Config(Term.Select(field, Term.Name("underlying"))), Term.Name("getBoolean")), List(key)) =>
          Patch.replaceTree(t, s"$field.get[Boolean]($key)")

        case t @ Config(Term.Apply(Term.Select(field, Term.Name("getInt")), List(key))) =>
          Patch.replaceTree(t, s"$field.getOptional[Int]($key)")
        case t @ JavaList(field, "getIntSeq", key, "intValue") =>
          Patch.replaceTree(t, s"$field.getOptional[Seq[Int]]($key)")
        case t @ Term.Apply(Term.Select(Config(Term.Select(field, Term.Name("underlying"))), Term.Name("getInt")), List(key)) =>
          Patch.replaceTree(t, s"$field.get[Int]($key)")

        case t @ Config(Term.Apply(Term.Select(field, Term.Name("getLong")), List(key))) =>
          Patch.replaceTree(t, s"$field.getOptional[Long]($key)")
        case t @ JavaList(field, "getLongSeq", key, "longValue") =>
          Patch.replaceTree(t, s"$field.getOptional[Seq[Long]]($key)")
        case t @ Term.Apply(Term.Select(Config(Term.Select(field, Term.Name("underlying"))), Term.Name("getLong")), List(key)) =>
          Patch.replaceTree(t, s"$field.get[Long]($key)")

        case t @ Config(Term.Apply(Term.Select(field, Term.Name("getDouble")), List(key))) =>
          Patch.replaceTree(t, s"$field.getOptional[Double]($key)")
        case t @ JavaList(field, "getDoubleSeq", key, "doubleValue") =>
          Patch.replaceTree(t, s"$field.getOptional[Seq[Double]]($key)")
        case t @ Term.Apply(Term.Select(Config(Term.Select(field, Term.Name("underlying"))), Term.Name("getDouble")), List(key)) =>
          Patch.replaceTree(t, s"$field.get[Double]($key)")

        case t @ Config(Term.Apply(Term.Select(field, Term.Name("getNumber")), List(key))) =>
          Patch.replaceTree(t, s"$field.getOptional[Number]($key)")
        case t @ Config(Term.Apply(Term.Select(field, Term.Name("getNumberSeq")), List(key))) =>
          Patch.replaceTree(t, s"$field.getOptional[Seq[Number]]($key)")
        case t @ Term.Apply(Term.Select(Config(Term.Select(field, Term.Name("underlying"))), Term.Name("getNumber")), List(key)) =>
          Patch.replaceTree(t, s"$field.get[Number]($key)")
      }
      .asPatch
      .atomic
  }
}
