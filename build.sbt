lazy val V = _root_.scalafix.sbt.BuildInfo

lazy val rulesCrossVersions = Seq(V.scala213, V.scala212, V.scala211)

inThisBuild(
  List(
    organization := "com.typesafe.play.contrib",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions ++= List("-Yrangepos"),
    versionScheme := Some("semver-spec"),
    dynverSeparator := "-",
    updateOptions := updateOptions.value.withLatestSnapshots(false),
    resolvers += Resolver.typesafeRepo("releases"),
    resolvers += Resolver.typesafeIvyRepo("releases"),
    credentials ++= (for {
      usr <- sys.env.get("GITHUB_ACTOR")
      pwd <- sys.env.get("GITHUB_TOKEN")
    } yield Credentials("GitHub Package Registry", "maven.pkg.github.com", usr, pwd)).toList,
    publishTo := Some("github" at "https://maven.pkg.github.com/meier-christoph/play-migrations-scalafix-rules")
  )
)

lazy val play23 = "2.3.10"
lazy val play24 = "2.4.11"
lazy val play25 = "2.5.19"
lazy val play26 = "2.6.25"
lazy val play27 = "2.7.9"
lazy val play28 = "2.8.19"

lazy val core = projectMatrix
  .settings(
    moduleName := "play-migrations-core",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

// migrations from v2.3.x to v2.4.x

lazy val rules24 = projectMatrix
  .in(file("play-v2.3.x-to-v2.4.x/rules"))
  .dependsOn(core)
  .settings(
    moduleName := "play-migrations-v23-to-v24-scalafix-rules",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

lazy val adapters24 = projectMatrix
  .in(file("play-v2.3.x-to-v2.4.x/adapters"))
  .settings(
    moduleName := "play-migrations-v23-to-v24-adapters",
    libraryDependencies ++= List(
      "javax.inject" % "javax.inject" % "1"
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

lazy val input24 = projectMatrix
  .in(file("play-v2.3.x-to-v2.4.x/input"))
  .dependsOn(adapters24)
  .settings(publish / skip := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play23,
      "com.typesafe.play" %% "play-cache" % play23,
      "com.typesafe.play" %% "play-jdbc" % play23,
      "com.typesafe.play" %% "play-ws" % play23,
      "com.typesafe.play" %% "anorm" % play23
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

lazy val output24 = projectMatrix
  .in(file("play-v2.3.x-to-v2.4.x/output"))
  .settings(publish / skip := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play24,
      "com.typesafe.play" %% "play-cache" % play24,
      "com.typesafe.play" %% "play-jdbc" % play24,
      "com.typesafe.play" %% "play-ws" % play24,
      "com.typesafe.play" %% "anorm" % "2.4.0"
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

lazy val tests24 = projectMatrix
  .in(file("play-v2.3.x-to-v2.4.x/tests"))
  .settings(
    publish / skip := true,
    libraryDependencies ++= List(
      "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full
    ),
    scalafixTestkitOutputSourceDirectories :=
      TargetAxis.resolve(output24, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      TargetAxis.resolve(input24, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath :=
      TargetAxis.resolve(input24, Compile / fullClasspath).value,
    scalafixTestkitInputScalacOptions :=
      TargetAxis.resolve(input24, Compile / scalacOptions).value,
    scalafixTestkitInputScalaVersion :=
      TargetAxis.resolve(input24, Compile / scalaVersion).value
  )
  .dependsOn(rules24)
  .enablePlugins(ScalafixTestkitPlugin)
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

// migrations from v2.4.x to v2.4.x

lazy val rules25 = projectMatrix
  .in(file("play-v2.4.x-to-v2.5.x/rules"))
  .dependsOn(core)
  .settings(
    moduleName := "play-migrations-v24-to-v25-scalafix-rules",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

lazy val adapters25 = projectMatrix
  .in(file("play-v2.4.x-to-v2.5.x/adapters"))
  .settings(
    moduleName := "play-migrations-v24-to-v25-adapters",
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play24 % Optional,
      "com.typesafe.play" %% "play-cache" % play24 % Optional,
      "com.typesafe.play" %% "play-jdbc" % play24 % Optional,
      "com.typesafe.play" %% "play-ws" % play24 % Optional,
      "com.typesafe.play" %% "anorm" % "2.4.0" % Optional
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

lazy val input25 = projectMatrix
  .in(file("play-v2.4.x-to-v2.5.x/input"))
  .dependsOn(adapters26)
  .settings(publish / skip := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play24,
      "com.typesafe.play" %% "play-cache" % play24,
      "com.typesafe.play" %% "play-jdbc" % play24,
      "com.typesafe.play" %% "play-ws" % play24,
      "com.typesafe.play" %% "anorm" % "2.4.0"
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

lazy val output25 = projectMatrix
  .in(file("play-v2.4.x-to-v2.5.x/output"))
  .settings(publish / skip := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play25,
      "com.typesafe.play" %% "play-cache" % play25,
      "com.typesafe.play" %% "play-jdbc" % play25,
      "com.typesafe.play" %% "play-ws" % play25,
      "com.typesafe.play" %% "anorm" % "2.5.3"
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

lazy val tests25 = projectMatrix
  .in(file("play-v2.4.x-to-v2.5.x/tests"))
  .settings(
    publish / skip := true,
    libraryDependencies ++= List(
      "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full
    ),
    scalafixTestkitOutputSourceDirectories :=
      TargetAxis.resolve(output25, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      TargetAxis.resolve(input25, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath :=
      TargetAxis.resolve(input25, Compile / fullClasspath).value,
    scalafixTestkitInputScalacOptions :=
      TargetAxis.resolve(input25, Compile / scalacOptions).value,
    scalafixTestkitInputScalaVersion :=
      TargetAxis.resolve(input25, Compile / scalaVersion).value
  )
  .dependsOn(rules25)
  .enablePlugins(ScalafixTestkitPlugin)
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

// migrations from v2.5.x to v2.6.x

lazy val rules26 = projectMatrix
  .in(file("play-v2.5.x-to-v2.6.x/rules"))
  .dependsOn(core)
  .settings(
    moduleName := "play-migrations-v25-to-v26-scalafix-rules",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

lazy val adapters26 = projectMatrix
  .in(file("play-v2.5.x-to-v2.6.x/adapters"))
  .settings(
    moduleName := "play-migrations-v25-to-v26-adapters",
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play25 % Optional,
      "com.typesafe.play" %% "play-cache" % play25 % Optional,
      "com.typesafe.play" %% "play-ws" % play25 % Optional
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

lazy val input26 = projectMatrix
  .in(file("play-v2.5.x-to-v2.6.x/input"))
  .dependsOn(adapters26)
  .settings(publish / skip := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play25,
      "com.typesafe.play" %% "play-cache" % play25,
      "com.typesafe.play" %% "play-ws" % play25
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

lazy val output26 = projectMatrix
  .in(file("play-v2.5.x-to-v2.6.x/output"))
  .settings(publish / skip := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play26,
      "com.typesafe.play" %% "play-cache" % play26,
      "com.typesafe.play" %% "play-ws" % play26
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

lazy val tests26 = projectMatrix
  .in(file("play-v2.5.x-to-v2.6.x/tests"))
  .settings(
    publish / skip := true,
    libraryDependencies ++= List(
      "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full
    ),
    scalafixTestkitOutputSourceDirectories :=
      TargetAxis.resolve(output26, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      TargetAxis.resolve(input26, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath :=
      TargetAxis.resolve(input26, Compile / fullClasspath).value,
    scalafixTestkitInputScalacOptions :=
      TargetAxis.resolve(input26, Compile / scalacOptions).value,
    scalafixTestkitInputScalaVersion :=
      TargetAxis.resolve(input26, Compile / scalaVersion).value
  )
  .dependsOn(rules26)
  .enablePlugins(ScalafixTestkitPlugin)
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala211))

// migrations from v2.6.x to v2.7.x

lazy val rules27 = projectMatrix
  .in(file("play-v2.6.x-to-v2.7.x/rules"))
  .dependsOn(core)
  .settings(
    moduleName := "play-migrations-v26-to-v27-scalafix-rules",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

lazy val adapters27 = projectMatrix
  .in(file("play-v2.6.x-to-v2.7.x/adapters"))
  .settings(
    moduleName := "play-migrations-v26-to-v27-adapters",
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play26 % Optional
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala212, V.scala211))

lazy val input27 = projectMatrix
  .in(file("play-v2.6.x-to-v2.7.x/input"))
  .dependsOn(adapters27)
  .settings(publish / skip := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play26,
      "com.typesafe.play" %% "play-cache" % play26,
      "com.typesafe.play" %% "play-ws" % play26
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala212))

lazy val output27 = projectMatrix
  .in(file("play-v2.6.x-to-v2.7.x/output"))
  .settings(publish / skip := true)
  .settings(
    libraryDependencies ++= List(
      "com.typesafe.play" %% "play" % play27,
      "com.typesafe.play" %% "play-cache" % play27,
      "com.typesafe.play" %% "play-ws" % play27
    )
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala212))

lazy val tests27 = projectMatrix
  .in(file("play-v2.6.x-to-v2.7.x/tests"))
  .settings(
    publish / skip := true,
    libraryDependencies ++= List(
      "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full
    ),
    scalafixTestkitOutputSourceDirectories :=
      TargetAxis.resolve(output27, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      TargetAxis.resolve(input27, Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath :=
      TargetAxis.resolve(input27, Compile / fullClasspath).value,
    scalafixTestkitInputScalacOptions :=
      TargetAxis.resolve(input27, Compile / scalacOptions).value,
    scalafixTestkitInputScalaVersion :=
      TargetAxis.resolve(input27, Compile / scalaVersion).value
  )
  .dependsOn(rules27)
  .enablePlugins(ScalafixTestkitPlugin)
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(List(V.scala212))
