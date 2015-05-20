import sbtassembly.MergeStrategy

name := """IOSR-streaming-data"""

organization := "pl.edu.agh.iosr.iosr2015.data.streaming.twitter"

version := "0.1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Dependencies.sparkAkkaHadoop

//excludeDependencies ++= Seq(
////  "org.eclipse.jetty.orbit" % "javax.servlet",
//  "commons-beanutils" % "commons-beanutils",
//  "org.apache.hadoop" % "hadoop-yarn-api",
//  "com.typesafe.akka" %% "akka-actor",
////  "com.codahale.metrics" % "metrics-core",
//  "commons-logging" % "commons-logging",
//  //  "commons-collections" % "commons-collections",
//  //  "com.google.guava" % "guava",
//  "org.apache.spark" %% "spark-network-common",
//  //  "com.esotericsoftware.minlog" % "minlog",
//  "org.spark-project.spark" % "unused"
////  "org.apache.spark" %% "spark-network-shuffle"
//)

resolvers += Resolver.sonatypeRepo("releases")

resolvers += Resolver.sonatypeRepo("snapshots")

// Load Assembly Settings

//val sharedMergeStrategy: (String => MergeStrategy) => String => MergeStrategy =
//  old => {
//    case x if x.startsWith("META-INF/ECLIPSEF.RSA") => MergeStrategy.last
//    case x if x.startsWith("META-INF/mailcap") => MergeStrategy.last
//    case x if x.endsWith("plugin.properties") => MergeStrategy.last
//    case x => old(x)
//  }

// Assembly App
assemblyJarName in assembly := name.value + "-" + version.value + ".jar"

//assemblyMergeStrategy in assembly <<= (assemblyMergeStrategy in assembly)(sharedMergeStrategy)

test in assembly := {} // disable tests in assembly

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf" => MergeStrategy.concat
  case "unwanted.txt" => MergeStrategy.discard
  case PathList("org", "apache", "hadoop", "yarn", "util", "package-info.class") => MergeStrategy.first
  case PathList("org", "apache", "hadoop", "yarn", "factory", "package-info.class") => MergeStrategy.first
  case PathList("org", "apache", "hadoop", "yarn", "factory", "providers", "package-info.class") => MergeStrategy.first
  case PathList("org", "apache", "hadoop", "yarn", "factories", "package-info.class") => MergeStrategy.first
  case PathList("org", "apache", "spark", "unused", "UnusedStubClass.class") => MergeStrategy.first
  case PathList("com", "google", "common", "base", "Optional.class") => MergeStrategy.first
  case PathList("com", "google", "common", "base", "Optional$1.class") => MergeStrategy.first
  case PathList("com", "google", "common", "base", "Optional$1$1.class") => MergeStrategy.first
  case PathList("com", "google", "common", "base", "Function.class") => MergeStrategy.first
  case PathList("com", "google", "common", "base", "Absent.class") => MergeStrategy.first
  case PathList("com", "google", "common", "base", "Supplier.class") => MergeStrategy.first
  case PathList("com", "google", "common", "base", "Present.class") => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}