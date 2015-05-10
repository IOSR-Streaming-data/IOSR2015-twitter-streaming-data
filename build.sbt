
name := """IOSR-streaming-data"""

lazy val commonSettings = Seq(
  organization := "pl.edu.agh.iosr.iosr2015.data.streaming.twitter",
  version := "0.1.0",
  scalaVersion := "2.11.6"
)

lazy val core = project
  .settings(commonSettings: _*)

lazy val cass = project.dependsOn(core)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "1.2.0"
  )

lazy val root = project.in(file(".")).aggregate(core, cass)
  .settings(commonSettings: _*)
