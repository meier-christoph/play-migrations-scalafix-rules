lazy val V = _root_.scalafix.sbt.BuildInfo
inThisBuild(
  List(
    organization := "com.typesafe.play.contrib",
    scalaVersion := V.scala211,
    crossScalaVersions := List(V.scala211, V.scala212),
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List("-Yrangepos"),
    updateOptions := updateOptions.value.withLatestSnapshots(false)
  )
)

skip in publish := true

lazy val play25 = "2.5.19"
lazy val play26 = "2.6.22"

lazy val core = project
  .settings(
    moduleName := "play-migrations-core",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )

// migrations from v2.5.x to v2.6.x

lazy val rules25 = project
  .in(file("play-v2.5.x-to-v2.6.x/rules"))
  .dependsOn(core)
  .settings(
    moduleName := "play-migrations-v25-to-v26-scalafix-rules",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )

lazy val input25 = project
  .in(file("play-v2.5.x-to-v2.6.x/input"))
  .settings(skip in publish := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play25,
      "com.typesafe.play" %% "play-cache" % play25,
      "com.typesafe.play" %% "play-ws" % play25
    )
  )

lazy val output25 = project
  .in(file("play-v2.5.x-to-v2.6.x/output"))
  .settings(skip in publish := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play26,
      "com.typesafe.play" %% "play-cache" % play26,
      "com.typesafe.play" %% "play-ws" % play26
    )
  )

lazy val tests25 = project
  .in(file("play-v2.5.x-to-v2.6.x/tests"))
  .settings(
    skip in publish := true,
    libraryDependencies ++= List(
      "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full
    ),
    compile.in(Compile) :=
      compile.in(Compile).dependsOn(compile.in(input25, Compile)).value,
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output25, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input25, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input25, Compile).value
  )
  .dependsOn(rules25)
  .enablePlugins(ScalafixTestkitPlugin)
