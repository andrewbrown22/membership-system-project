import play.sbt.routes.RoutesKeys

name := """membership-system-project"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.5"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.18.1-play27"
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % Test

routesGenerator := InjectedRoutesGenerator
RoutesKeys.routesImport += "models.Card"