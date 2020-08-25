# play-migrations-scalafix-rules

### Usage

Start by installing the sbt plugin in `project/plugins.sbt`

```scala
// project/plugins.sbt
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.19")
```

... then add custom rules

```scala
// build.sbt
scalafixDependencies in ThisBuild ++= List(
  "com.typesafe.play.contrib" %% "play-migrations-v25-to-v26-scalafix-rules" % "0.1.0-SNAPSHOT",
  "com.typesafe.play.contrib" %% "play-migrations-v26-to-v27-scalafix-rules" % "0.1.0-SNAPSHOT"
)
```

```sh
$ sbt
> scalafixEnable
> scalafix MigrateControllers
```

... then remove the plugin and all rules once you're done.

### Advanced Usage

When you have multiple projects that need migration, it is easier to enable the plugin globally 
in `~/.sbt/0.13/` instead of editing each project.

```scala
// ~/.sbt/0.13/plugins/plugins.sbt
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.19")
```

```scala
// ~/.sbt/0.13/plugins/PlayFixPlugin.scala
import sbt._
import Keys._
import scalafix.sbt.ScalafixPlugin
import scalafix.sbt.ScalafixPlugin.autoImport._

object PlayFixPlugin extends AutoPlugin {
  override def trigger: PluginTrigger = allRequirements
  override def requires: Plugins = ScalafixPlugin
  override def buildSettings: Seq[Setting[_]] =
    List(
      scalafixDependencies ++= List(
        "com.typesafe.play.contrib" %% "play-migrations-v25-to-v26-scalafix-rules" % "0.1.0-SNAPSHOT",
        "com.typesafe.play.contrib" %% "play-migrations-v26-to-v27-scalafix-rules" % "0.1.0-SNAPSHOT"
      ),
      scalafixConfig := Some(file("~/shared/scalafix.conf")) // see examples
    )
}
```

... then run the rules in any sbt 0.13.x project

```sh
$ sbt
> scalafixEnable
> scalafix
```

... it will use the global plugin and run all migrations you specified in the config file.

### For developers:

Run only a single test :

```sh
$ sbt
> tests25/testOnly -- -z WS
```
