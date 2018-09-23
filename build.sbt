import sbt.Keys.scalaVersion

lazy val main = (project in file("."))
  .settings(
    name := "akka-crm",
    version := "0.1",
    scalaVersion := "2.12.6",
    libraryDependencies ++= Seq(
      "joda-time" % "joda-time" % "2.10",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.3",
      "com.typesafe.akka" %% "akka-actor" % "2.5.13",
      "com.typesafe.akka" %% "akka-persistence" % "2.5.14",
      "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "com.typesafe.akka" %% "akka-http" % "10.1.3",
      "com.typesafe.akka" %% "akka-stream" % "2.5.12",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.3" % Test
    )
  )

