import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.5.10"
  lazy val scalajHttp = "org.scalaj" %% "scalaj-http" % "2.4.1"
  lazy val jsoup = "org.jsoup" % "jsoup" % "1.11.3"
  lazy val typesafeConfig = "com.typesafe" % "config" % "1.3.2"
  lazy val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0"
}
