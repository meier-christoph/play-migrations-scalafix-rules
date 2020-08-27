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

lazy val play23 = "2.3.10"
lazy val play24 = "2.4.11"
lazy val play25 = "2.5.19"
lazy val play26 = "2.6.25"
lazy val play27 = "2.7.5"
lazy val play28 = "2.8.2"

lazy val core = project
  .settings(
    moduleName := "play-migrations-core",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )

// migrations from v2.3.x to v2.4.x

lazy val rules24 = project
  .in(file("play-v2.3.x-to-v2.4.x/rules"))
  .dependsOn(core)
  .settings(
    moduleName := "play-migrations-v23-to-v24-scalafix-rules",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )

lazy val adapters24 = project
  .in(file("play-v2.3.x-to-v2.4.x/adapters"))
  .settings(
    moduleName := "play-migrations-v23-to-v24-adapters",
    libraryDependencies ++= List(
      "javax.inject" % "javax.inject" % "1"
    )
  )

lazy val input24 = project
  .in(file("play-v2.3.x-to-v2.4.x/input"))
  .dependsOn(adapters24)
  .settings(skip in publish := true)
  .settings(
    crossScalaVersions := List(V.scala211),
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play23,
      "com.typesafe.play" %% "play-cache" % play23,
      "com.typesafe.play" %% "play-jdbc" % play23,
      "com.typesafe.play" %% "play-ws" % play23,
      "com.typesafe.play" %% "anorm" % play23
    )
  )

lazy val output24 = project
  .in(file("play-v2.3.x-to-v2.4.x/output"))
  .settings(skip in publish := true)
  .settings(
    crossScalaVersions := List(V.scala211),
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play24,
      "com.typesafe.play" %% "play-cache" % play24,
      "com.typesafe.play" %% "play-jdbc" % play24,
      "com.typesafe.play" %% "play-ws" % play24,
      "com.typesafe.play" %% "anorm" % "2.4.0"
    )
  )

lazy val tests24 = project
  .in(file("play-v2.3.x-to-v2.4.x/tests"))
  .settings(
    skip in publish := true,
    crossScalaVersions := List(V.scala211),
    libraryDependencies ++= List(
      "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full
    ),
    compile.in(Compile) :=
      compile.in(Compile).dependsOn(compile.in(input24, Compile)).value,
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output24, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input24, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input24, Compile).value
  )
  .dependsOn(rules24)
  .enablePlugins(ScalafixTestkitPlugin)

// migrations from v2.5.x to v2.6.x

lazy val rules26 = project
  .in(file("play-v2.5.x-to-v2.6.x/rules"))
  .dependsOn(core)
  .settings(
    moduleName := "play-migrations-v25-to-v26-scalafix-rules",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )

lazy val input26 = project
  .in(file("play-v2.5.x-to-v2.6.x/input"))
  .settings(skip in publish := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play25,
      "com.typesafe.play" %% "play-cache" % play25,
      "com.typesafe.play" %% "play-ws" % play25
    )
  )

lazy val output26 = project
  .in(file("play-v2.5.x-to-v2.6.x/output"))
  .settings(skip in publish := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play26,
      "com.typesafe.play" %% "play-cache" % play26,
      "com.typesafe.play" %% "play-ws" % play26
    )
  )

lazy val tests26 = project
  .in(file("play-v2.5.x-to-v2.6.x/tests"))
  .settings(
    skip in publish := true,
    libraryDependencies ++= List(
      "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full
    ),
    compile.in(Compile) :=
      compile.in(Compile).dependsOn(compile.in(input26, Compile)).value,
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output26, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input26, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input26, Compile).value
  )
  .dependsOn(rules26)
  .enablePlugins(ScalafixTestkitPlugin)

// migrations from v2.6.x to v2.7.x

lazy val rules27 = project
  .in(file("play-v2.6.x-to-v2.7.x/rules"))
  .dependsOn(core)
  .settings(
    moduleName := "play-migrations-v26-to-v27-scalafix-rules",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )

lazy val input27 = project
  .in(file("play-v2.6.x-to-v2.7.x/input"))
  .settings(skip in publish := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play26,
      "com.typesafe.play" %% "play-cache" % play26,
      "com.typesafe.play" %% "play-ws" % play26
    )
  )

lazy val output27 = project
  .in(file("play-v2.6.x-to-v2.7.x/output"))
  .settings(skip in publish := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play27,
      "com.typesafe.play" %% "play-cache" % play27,
      "com.typesafe.play" %% "play-ws" % play27
    )
  )

lazy val tests27 = project
  .in(file("play-v2.6.x-to-v2.7.x/tests"))
  .settings(
    skip in publish := true,
    libraryDependencies ++= List(
      "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full
    ),
    compile.in(Compile) :=
      compile.in(Compile).dependsOn(compile.in(input27, Compile)).value,
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output27, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input27, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input27, Compile).value
  )
  .dependsOn(rules27)
  .enablePlugins(ScalafixTestkitPlugin)
