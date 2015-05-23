import sbt.Keys._

name := """IOSR-streaming-data"""

organization := "pl.edu.agh.iosr.iosr2015.data.streaming.twitter"

version := "0.1.0"

scalaVersion := "2.10.4"

resolvers += "cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos/"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.12.3" % "test" withSources() withJavadoc(),
  "org.apache.spark" %% "spark-core" % "1.3.1" % "provided" withSources() withJavadoc(),
  "org.apache.spark" %% "spark-streaming" % "1.3.1" % "provided" withSources() withJavadoc(),
  "org.apache.spark" %% "spark-streaming-twitter" % "1.3.1" withSources() withJavadoc(),
  //  "org.apache.spark" %% "spark-sql" % "1.3.1" % "provided" withSources() withJavadoc(),
  //  "org.apache.spark" %% "spark-hive" % "1.3.1" % "provided" withSources() withJavadoc(),
  //  "org.apache.spark" %% "spark-mllib" % "1.3.1" % "provided" withSources() withJavadoc(),
  //  "org.apache.spark" %% "spark-graphx" % "1.3.1" % "provided" withSources() withJavadoc(),
  "org.apache.hadoop" % "hadoop-client" % "2.5.0-cdh5.3.3" % "provided" withJavadoc(),
  "com.github.scopt" %% "scopt" % "3.2.0",
  "joda-time" % "joda-time" % "2.7",
  "io.github.morgaroth" %% "spark-cassandra-connector" % "1.3.0-SNAPSHOT" withSources() withJavadoc(),
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.5" withSources() withJavadoc()
)

excludeDependencies += "org.spark-project.spark" % "unused"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

assemblyJarName in assembly := name.value + "-" + version.value + ".jar"

run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in(Compile, run), runner in(Compile, run))

test in assembly := {}
