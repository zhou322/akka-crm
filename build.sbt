import sbt.Keys.scalaVersion

lazy val main = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "akka-crm",
    fork := true,
    version := "0.1",
    scalaVersion := "2.12.6",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-encoding", "UTF-8",       // yes, this is 2 args
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      // "-Xfatal-warnings",      Deprecations keep from enabling this
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Xfuture",
      "-Ywarn-unused-import",     // 2.11 only
      "-target:jvm-1.8"
    ),
    libraryDependencies ++= Seq(
      "joda-time" % "joda-time" % "2.10",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.3",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.twitter" %% "chill-akka" % "0.8.4",
      "com.typesafe.akka" %% "akka-actor" % "2.5.13",
      "com.typesafe.akka" %% "akka-persistence" % "2.5.14",
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.90",
      "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % "0.90" % Test,
      "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "com.typesafe.akka" %% "akka-http" % "10.1.3",
      "com.typesafe.akka" %% "akka-stream" % "2.5.12",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.3" % Test,
      "com.github.dnvriend" %% "akka-persistence-inmemory" % "2.5.1.1" % Test
    ),
    resolvers += Resolver.jcenterRepo
  ).settings(allJavaOption: _*)

lazy val allJavaOption: Seq[sbt.Def.SettingsDefinition] = Seq(
  javaOptions in Test += "-Dconfig.file=src/test/resources/test.conf"
)