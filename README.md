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


### For developers:

Run only a single test :

```
sbt tests25/testOnly -- -z WS
```
