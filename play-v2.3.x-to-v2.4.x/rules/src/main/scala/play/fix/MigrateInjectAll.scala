package play.fix

import metaconfig.Configured
import scalafix.v1._

import scala.collection.mutable
import scala.meta._
import scala.meta.transversers.SimpleTraverser

final class MigrateInjectAll(config: MigrateInjectAllConfig) extends SemanticRule("MigrateInjectAll") {
  def this() = this(MigrateInjectAllConfig.default)

  override def withConfiguration(config: Configuration): Configured[Rule] = {
    config.conf
      .getOrElse("MigrateInjectAll")(this.config)
      .map(newConfig => new MigrateInjectAll(newConfig))
  }

  val inject: Mod.Annot = Mod.Annot(Init(Type.Name("Inject"), Name.Anonymous(), List(Nil)))

  def makeParam(name: String, clazz: String): Term.Param = {
    Term.Param(List(Mod.ValParam()), Name(name), Some(Type.Name(clazz)), None)
  }

  val nextTypes = new mutable.ListBuffer[Symbol]()

  override def beforeStart(): Unit = {
    nextTypes.clear()
  }

  override def afterComplete(): Unit = {
    val next = nextTypes
      .map(_.toFQCN)
      .filter(_.nonEmpty)
    if (next.nonEmpty) {
      println("Rules for next run:")
      println("-----")
      println("MigrateInjectAll.types = [")
      var first = true
      next.foreach { name =>
        if (first) {
          first = false
          print(s"""  "$name"""")
        } else {
          print(s""",\n  "$name"""")
        }
      }
      print("\n")
      println("]")
      println("-----")
    }
    ()
  }

  override def fix(implicit doc: SemanticDocument): Patch = {
    if (config.types.isEmpty) {
      fixPlay
    } else {
      fixCustom
    }
  }

  def fixPlay(implicit doc: SemanticDocument): Patch = {
    val imports = ImportHolder(doc.tree)

    object WS {
      def unapply(t: Term): Option[Term] = {
        if (t.symbol.value == "play/api/libs/ws/WS.") {
          Some(t)
        } else None
      }
    }

    def replaceWSClient(tree: Tree): List[Patch] = {
      tree.collect {
        case t @ WS(_) =>
          Patch.replaceTree(t, "_wsClient")
      }
    }

    object DB {
      def unapply(t: Term): Option[Term] = {
        if (t.symbol.value == "play/api/db/DB.") {
          Some(t)
        } else None
      }
    }

    def replaceDatabase(tree: Tree): List[Patch] = {
      tree.collect {
        case t @ DB(_) =>
          Patch.replaceTree(t, "_database")
      }
    }

    object Cache {
      def unapply(t: Term): Option[Term] = {
        if (t.symbol.value == "play/api/cache/Cache.") {
          Some(t)
        } else None
      }
    }

    def replaceCache(tree: Tree): List[Patch] = {
      tree.collect {
        case t @ Cache(_) =>
          Patch.replaceTree(t, "_cache")
      }
    }

    val buf = scala.collection.mutable.ListBuffer[Patch]()

    def fixClass(tree: Tree, t: Defn.Class, fix: Defn.Class => Patch): Boolean = {
      var fixIt = false
      var fixed = t.ensureAnnot(inject)

      val db = replaceDatabase(tree)
      if (db.nonEmpty) {
        fixIt = true
        buf += db.asPatch
        imports.ensure(importer"play.api.db.Database")
        fixed = fixed.ensureParam(makeParam("_database", "Database"))
      }

      val ws = replaceWSClient(tree)
      if (ws.nonEmpty) {
        fixIt = true
        buf += ws.asPatch
        imports.ensure(importer"play.api.libs.ws.WSClient")
        fixed = fixed.ensureParam(makeParam("_wsClient", "WSClient"))
      }

      val cache = replaceCache(tree)
      if (cache.nonEmpty) {
        fixIt = true
        buf += cache.asPatch
        imports.ensure(importer"play.api.cache.CacheApi")
        fixed = fixed.ensureParam(makeParam("_cache", "CacheApi"))
      }

      if (fixIt) {
        nextTypes += t.symbol
        imports.ensure(importer"javax.inject.Inject")
        buf += fix(fixed)
      }

      fixIt
    }

    def fixTrait(tree: Tree, t: Defn.Trait, fix: List[(String, String)] => Patch): Boolean = {
      var fixIt = false
      val fixed = scala.collection.mutable.ListBuffer[(String, String)]()

      val db = replaceDatabase(tree)
      if (db.nonEmpty) {
        fixIt = true
        buf += db.asPatch
        imports.ensure(importer"play.api.db.Database")
        fixed += "_database" -> "Database"
      }

      val ws = replaceWSClient(tree)
      if (ws.nonEmpty) {
        fixIt = true
        buf += ws.asPatch
        imports.ensure(importer"play.api.libs.ws.WSClient")
        fixed += "_wsClient" -> "WSClient"
      }

      val cache = replaceCache(tree)
      if (cache.nonEmpty) {
        fixIt = true
        buf += cache.asPatch
        imports.ensure(importer"play.api.cache.CacheApi")
        fixed += "_cache" -> "CacheApi"
      }

      if (fixIt) {
        nextTypes += t.symbol
        buf += fix(fixed.toList.distinct)
      }

      fixIt
    }

    object traverser extends SimpleTraverser {
      override def apply(tree: Tree): Unit = {
        tree match {
          case t @ Defn.Class(_, _, _, _, _) =>
            fixClass(t, t, fix => ExtraPatch.replaceClassDef(t, fix))
          case t @ Defn.Object(_, Term.Name(n), _) if !config.types.contains(n) =>
            if (fixClass(t, t.toClass, fix => ExtraPatch.replaceObjectDef(t, fix))) {
              val companion =
                s"""|
                    |@deprecated("(scalafix) Migrate to DI", "2.3.0")
                    |object ${t.name} {
                    |  // FIXME(scalafix): Remove once migration is completed ("${t.symbol.toFQCN}")
                    |  lazy val _instance: ${t.name} = Play.current.injector.instanceOf[${t.name}]
                    |  implicit def _instance(f: ${t.name}.type): ${t.name} = f._instance
                    |}
                    |""".stripMargin
              imports.ensure(importer"play.api.Play")
              buf += Patch.addRight(t, companion)
            }
          case t @ Defn.Trait(_, _, _, _, Template(_, _, _, bdy)) =>
            def addFields(f: List[(String, String)]): Patch = {
              f.map {
                case (n, c) =>
                  Patch.addRight(bdy.last, s"\n  def $n: $c = Play.current.injector.instanceOf[$c]\n")
              }.foldLeft(Patch.empty)(_ + _)
            }
            if (fixTrait(t, t, fix => addFields(fix))) {
              imports.ensure(importer"play.api.Play")
              val companion =
                s"""|
                    |@deprecated("(scalafix) Migrate to DI", "2.3.0")
                    |object ${t.name} {
                    |  // FIXME(scalafix): Remove once migration is completed ("${t.symbol.toFQCN}")
                    |  lazy val _instance: ${t.name} = Play.current.injector.instanceOf[${t.name}]
                    |  implicit def _instance(f: ${t.name}.type): ${t.name} = f._instance
                    |}
                    |""".stripMargin
              buf += Patch.addRight(t, companion)

              t.companion match {
                case Some(c @ Defn.Object(_, _, Template(_, _, _, Nil))) =>
                  buf += Patch.removeTokens(c.tokens)
                case _ =>
                  ()
              }
            }
          case _ =>
            super.apply(tree)
        }
      }
    }
    traverser(doc.tree)
    buf.toList.asPatch.atomic + imports.asPatch
  }

  def fixCustom(implicit doc: SemanticDocument): Patch = {
    val imports = ImportHolder(doc.tree)

    object ToReplace {
      val symbols: List[Symbol] = config.types.map { s =>
        Symbol(s.replace('.', '/') + ".")
      }
      def unapply(t: Term): Option[String] = {
        val sym = t.symbol
        symbols.find(_ == sym).map { _ =>
          t.syntax
        }
      }
    }

    val buf = scala.collection.mutable.ListBuffer[Patch]()

    def fixClass(tree: Tree, t: Defn.Class, fix: Defn.Class => Patch): Boolean = {
      var fixIt = false
      var fixed = t.ensureAnnot(inject)

      def replaceVariable(tree: Tree): List[Patch] = {
        tree.collect {
          case t @ ToReplace(name) =>
            fixed = fixed.ensureParam(makeParam(s"_$name", name))
            Patch.replaceTree(t, s"_$name")
        }
      }

      val cache = replaceVariable(tree)
      if (cache.nonEmpty) {
        fixIt = true
        buf += cache.asPatch
      }

      if (fixIt) {
        nextTypes += t.symbol
        imports.ensure(importer"javax.inject.Inject")
        buf += fix(fixed)
      }

      fixIt
    }

    object traverser extends SimpleTraverser {
      override def apply(tree: Tree): Unit = {
        tree match {
          case t @ Defn.Class(_, _, _, _, _) =>
            fixClass(t, t, fix => ExtraPatch.replaceClassDef(t, fix))
          case t @ Defn.Object(_, _, _) =>
            fixClass(t, t.toClass, fix => ExtraPatch.replaceObjectDef(t, fix))
          case _ =>
            super.apply(tree)
        }
      }
    }
    traverser(doc.tree)
    buf.toList.asPatch.atomic + imports.asPatch
  }
}
