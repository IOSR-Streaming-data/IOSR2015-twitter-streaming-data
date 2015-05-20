import sbt._

object Version {
  val spark     = "1.3.1"
  val hadoop    = "2.6.0"
  val slf4j     = "1.7.6"
  val logback   = "1.1.1"
  val scalaTest = "2.1.7"
  val mockito = "1.9.5"
  val akka = "2.3.9"
}

object Library {
  // workaround until 2.11 version for Spark Streaming's available
  var sparkCore      = "org.apache.spark"  %% "spark-core"              % Version.spark
  val sparkStreaming = "org.apache.spark"  %% "spark-streaming"         % Version.spark
  val sparkTwitter   = "org.apache.spark"  %% "spark-streaming-twitter" % Version.spark
  val akkaActor      = "com.typesafe.akka" %% "akka-actor"              % Version.akka
  val akkaTestKit    = "com.typesafe.akka" %% "akka-testkit"            % Version.akka
  val hadoopClient   = "org.apache.hadoop" %  "hadoop-client"           % Version.hadoop
  val slf4jApi       = "org.slf4j"         %  "slf4j-api"               % Version.slf4j
  val logbackClassic = "ch.qos.logback"    %  "logback-classic"         % Version.logback
  val scalaTest      = "org.scalatest"     %% "scalatest"               % Version.scalaTest
  val mockitoAll     = "org.mockito"       %  "mockito-all"             % Version.mockito
}

object Dependencies {

  import Library._

  val sparkAkkaHadoop = Seq(
    sparkCore % "provided",
    sparkStreaming % "provided",
    sparkTwitter.exclude("org.spark-project.spark", "unused"),
    akkaActor % "provided",
    akkaTestKit % "test",
    hadoopClient % "provided",
    logbackClassic % "test",
    scalaTest % "test",
    mockitoAll % "test",
    "io.github.morgaroth" %% "spark-cassandra-connector" % "1.3.0-SNAPSHOT",
    "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.5"
  )
}
