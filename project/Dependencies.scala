import sbt._

object Version {
  val spark = "1.3.1"
  val hadoop = "2.6.0"
  val slf4j = "1.7.6"
  val logback = "1.1.1"
  val scalaTest = "2.1.7"
  val mockito = "1.9.5"
  val akka = "2.3.9"
}

object Library {
  // workaround until 2.11 version for Spark Streaming's available
  val sparkStreaming = ("org.apache.spark" %% "spark-streaming" % Version.spark)
    .exclude("org.mortbay.jetty", "servlet-api").
    exclude("commons-beanutils", "commons-beanutils").
    exclude("commons-collections", "commons-collections").
    exclude("org.apache.hadoop", "hadoop-yarn-api").
    exclude("commons-logging", "commons-logging").
    exclude("", "akka-actor").
    exclude("com.esotericsoftware.minlog", "minlog")
  val sparkTwitter = ("org.apache.spark" %% "spark-streaming-twitter" % Version.spark)
    .exclude("org.mortbay.jetty", "servlet-api").
    exclude("commons-beanutils", "commons-beanutils-core").
    exclude("commons-collections", "commons-collections").
    exclude("org.apache.hadoop", "hadoop-yarn-api").
    exclude("commons-logging", "commons-logging").
    exclude("com.esotericsoftware.minlog", "minlog")
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % Version.akka
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % Version.akka
  val hadoopClient = ("org.apache.hadoop" % "hadoop-client" % Version.hadoop).
    exclude("commons-beanutils", "commons-beanutils").
    exclude("commons-logging", "commons-logging")
  val slf4jApi = "org.slf4j" % "slf4j-api" % Version.slf4j
  val logbackClassic = "ch.qos.logback" % "logback-classic" % Version.logback
  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
  val mockitoAll = "org.mockito" % "mockito-all" % Version.mockito
}

object Dependencies {

  import Library._

  val sparkAkkaHadoop = Seq(
    sparkStreaming,
    sparkTwitter,
//    akkaActor,
//    akkaTestKit,
    hadoopClient,
    logbackClassic % "test",
    scalaTest % "test",
    mockitoAll % "test",
    "io.github.morgaroth" %% "spark-cassandra-connector" % "1.3.0-SNAPSHOT",
    "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.5"
  )
}
