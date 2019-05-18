import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "analytics",
    libraryDependencies ++= Seq(scalaTest % Test
                              ,http4sDSL
                              ,http4sServer
                              ,doobieCore
                              ,doobieH2
                              ,h2
                              ,logback
                              ),
    scalacOptions ++= Seq("-Ypartial-unification")
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
