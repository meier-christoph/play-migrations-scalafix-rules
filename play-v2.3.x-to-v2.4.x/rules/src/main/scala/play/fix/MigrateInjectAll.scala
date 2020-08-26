package play.fix

import metaconfig.Configured
import scalafix.v1._

import scala.meta._
import scala.meta.transversers.SimpleTraverser

final class MigrateInjectAll(config: MigrateInjectAllConfig) extends SemanticRule("MigrateInjectAll") {
  def this() = this(MigrateInjectAllConfig.default)

  val inject: Mod.Annot = Mod.Annot(Init(Type.Name("Inject"), Name.Anonymous(), List(Nil)))

  override def withConfiguration(config: Configuration): Configured[Rule] = {
    config.conf
      .getOrElse("MigrateInjectAll")(this.config)
      .map(newConfig => new MigrateInjectAll(newConfig))
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

    def makeParam(name: String, clazz: String): Term.Param = {
      Term.Param(Nil, Name(name), Some(Type.Name(clazz)), None)
    }

    val buf = scala.collection.mutable.ListBuffer[Patch]()

    def fixClass(tree: Tree, t: Defn.Class, fix: Defn.Class => Patch): Boolean = {
      var fixIt = false
      var fixed = t.ensureAnnot(inject)
      imports.ensure(importer"javax.inject.Inject")

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
            if (fixClass(t, t.toClass, fix => ExtraPatch.replaceObjectDef(t, fix))) {
              val companion =
                s"""|
                    |object ${t.name} {
                    |  // FIXME(scalafix): Remove once migration is completed
                    |  lazy val _instance: ${t.name} = Play.current.injector.instanceOf[${t.name}]
                    |  implicit def _instance(f: ${t.name}.type): ${t.name} = f._instance
                    |}
                    |""".stripMargin
              imports.ensure(importer"play.api.Play")
              buf += Patch.addRight(t, companion)
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
    Patch.empty
  }
}
