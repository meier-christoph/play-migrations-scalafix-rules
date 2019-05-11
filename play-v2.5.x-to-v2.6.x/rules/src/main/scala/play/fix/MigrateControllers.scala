package play.fix

import play.fix.Classes._
import scalafix.v1._

import scala.meta._
import scala.meta.tokens.Token.LeftBrace

class MigrateControllers extends SemanticRule("MigrateControllers") {

  /*

  Defn.Class(
        List(),
        Type.Name("ServiceCtrl"),
        List(),
        Ctor.Primary(
          List(
            Mod.Annot(Init(Type.Name("Inject"), Name.Anonymous, List(List())))
          ),
          Name.Anonymous,
          List(
            List(
              Term.Param(
                List(),
                Term.Name("srv"),
                Some(Type.Name("SomeServiceWeInjectIntoOurControllers")),
                None
              )
            )
          )
        ),
        Template(
          List(),
          List(
            Init(Type.Name("Controller"), Name.Anonymous, List()),
            Init(Type.Name("SomeTraitWeNeedToKeep"), Name.Anonymous, List())
          ),
          Self(Name.Anonymous, None),
          List(
            Defn.Def(
              List(),
              Term.Name("index"),
              List(),

class SimpleCtrl @Inject() extends Controller
   */

  override def fix(implicit doc: SemanticDocument): Patch = {
//    println("Tree.structure: " + doc.tree.structure)
    var hasControllerImport = false
    doc.tree.collect {
      case Importer(q"play.api.mvc", importedTypes) =>
        importedTypes.collect {
          case i @ importee"Controller" =>
            hasControllerImport = true
            Patch.removeImportee(i) +
              Patch.addGlobalImport(importer"play.api.mvc.BaseController") +
              Patch.addGlobalImport(importer"play.api.mvc.ControllerComponents")
        }.asPatch

      case clazz @ Defn.Class(_, _, _, _, Template(_, inits, _, _)) if hasControllerImport && inits.map(_.tpe.syntax).contains("Controller") =>
        val ctrlComp = Term.Param(List(Mod.ValParam()), Name("controllerComponents"), Some(Type.Name("ControllerComponents")), None)
        val baseCtrl = Init(Type.Name("BaseController"), Name.Anonymous(), Nil)
        val ctrl = clazz
          .mapInit(i => baseCtrl +: i.filterNot(_.tpe.syntax == "Controller"))
          .mapParam {
            case Nil       => List(List(ctrlComp))
            case h :: tail => (h :+ ctrlComp) :: tail
          }
        ExtraPatch.replaceClassDef(clazz, ctrl)

//        clazz.tokens.foldLeft(Patch.empty) {
//          case (p, _: LeftBrace) => p
//          case (p, t)            => p + Patch.removeToken(t)
//        }

//        val bdy = body.headOption.map(_.tokens)
//        val tot: Int = tpl.tokens.length // 214
//        val met: Int = tpl.stats.map(_.tokens.length).sum // 154
//        val bdy: Int = tpl.self.tokens.length + tpl.early.map(_.tokens.length).sum + tpl.inits.map(_.tokens.length).sum // 6
//        println(tot + " - " + met + " - " + bdy)
//        println("tpl : " + tpl.tokens.slice(6 * 2, 214)) // 214

//        Patch.addLeft(tpl, "{{") + Patch.addRight(tpl, "}}")

//        Patch.removeTokens(t.tokens) + Patch.addLeft(t, ctrl.syntax) + Patch.addRight(t, tpl.tokens.slice(12, 214).syntax)
//        Patch.replaceTree(t.noBody, ctrl.syntax)
      //    Patch.empty
//        println(ctrl.ctor.syntax)
//
//        inits.collect {
//          case i if i.tpe.syntax == "Controller" => Patch.removeTokens(i.tokens)
//        }.asPatch +
//          Patch.addLeft(tpl, baseCtrl.syntax) +
//          Patch.removeTokens(ctor.tokens) +
//          Patch.addRight(ctor, ctrl.ctor.syntax)
//        Patch.replaceTree(t, ctrl.syntax)
    }.asPatch
  }
}
