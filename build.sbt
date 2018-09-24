import Dependencies._

lazy val commonSettings = Seq(
  organization := "net.yidooo",
  scalaVersion := "2.12.3",
  version      := "0.1.0-SNAPSHOT"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "scrawler",
    fork in Test := true)
  .aggregate(core, examples)

lazy val core = (project in file("scrawler-core"))
  .settings(
    commonSettings,
    name := "scrawler-core",

    libraryDependencies += scalaLogging,
    libraryDependencies += akkaActor,
    libraryDependencies += scalajHttp,
    libraryDependencies += jsoup,
    libraryDependencies += typesafeConfig,
    libraryDependencies += macwire,

    libraryDependencies += scalaTest % Test
  )

lazy val examples = (project in file("scrawler-examples"))
  .settings(
    commonSettings,
    name := "scrawler-examples"
  )
  .dependsOn(core)
