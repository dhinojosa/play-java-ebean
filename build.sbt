name := """borgfitness"""

organization := "com.xyzcorp"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  guice,
  ehcache,
  "com.h2database" % "h2" % "1.4.192",
  "org.webjars" % "bootstrap" % "3.3.6"
)
