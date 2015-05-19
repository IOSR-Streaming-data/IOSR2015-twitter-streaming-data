import xsbti.Predefined

name := """IOSR-streaming-data"""

lazy val commonSettings = Seq(
  organization := "pl.edu.agh.iosr.iosr2015.data.streaming.twitter",
  version := "0.1.0",
  scalaVersion := "2.11.6",
  libraryDependencies ++= Dependencies.sparkAkkaHadoop,
  resolvers += Resolver.sonatypeRepo("releases")
)

lazy val core = project
  .settings(commonSettings: _*)
  .settings(mainClass in assembly := Some("pl.edu.agh.iosr.iosr2015.data.streaming.twitter.Main"))

lazy val cassandra = project.dependsOn(core)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "1.2.0"
  )

lazy val root = project.in(file(".")).aggregate(core, cassandra)
  .settings(commonSettings: _*)

