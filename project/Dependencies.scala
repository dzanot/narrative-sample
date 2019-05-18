import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"

  lazy val h2Version = "1.4.197"
  lazy val h2 = "com.h2database" % "h2" % h2Version
  lazy val doobieVersion = "0.6.0"
  lazy val doobieCore = "org.tpolecat" %% "doobie-core" % doobieVersion
  lazy val doobieH2 = "org.tpolecat" %% "doobie-h2" % doobieVersion


  lazy val http4sVersion = "0.20.1"
  lazy val http4sDSL = "org.http4s" %% "http4s-dsl" % http4sVersion
  lazy val http4sServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion
  
  lazy val logback = "ch.qos.logback"  %  "logback-classic"     % "1.2.3"
}
