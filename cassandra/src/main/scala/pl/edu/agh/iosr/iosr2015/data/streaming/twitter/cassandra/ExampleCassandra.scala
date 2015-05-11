package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra

//import com.datastax.spark.connector.SparkContextFunctions

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import com.datastax.spark.connector._

import scala.util.Random

object ExampleCassandra extends ExampleCassandra {
  def main(args: Array[String]) {
    val (sc: SparkContextFunctions, keyspace: String, table: String) = initialize()
    saveSomeData(sc, keyspace, table)
  }
}

trait ExampleCassandra {

  def saveSomeData(sc: SparkContextFunctions, keyspace: String, table: String): Unit = {
    val data = 1 to 10 map { i =>
      (s"id$i", Random.nextLong(), Random.alphanumeric.take(10).mkString)
    }

    val a: RDD[(String, Long, String)] = sc.sc.parallelize(data)
    a.saveToCassandra(keyspace, table, SomeColumns("id", "count", "word"))
  }

  def initialize(host: Option[String] = None): (SparkContextFunctions, String, String) = {
    /** Configures Spark. */
    val conf = new SparkConf(true)
      .set("spark.cassandra.connection.host", host.getOrElse("localhost"))
      .setMaster("local[*]")
      .setAppName("ExampleCassandra")

    /** Connect to the Spark cluster: */
    lazy val sc: SparkContextFunctions = new SparkContextFunctions(new SparkContext(conf))

    val keyspace = "test"
    val table = "test_table"
    case class Foo(id: String, count: Long, word: String)
    CassandraUtils.createNamespace(keyspace, conf)
    CassandraUtils.createTable(keyspace, table, "id" -> "text", List("word" -> "text", "count" -> "int"), conf)
    CassandraUtils.truncate(keyspace, table, conf)
    (sc, keyspace, table)
  }
}
