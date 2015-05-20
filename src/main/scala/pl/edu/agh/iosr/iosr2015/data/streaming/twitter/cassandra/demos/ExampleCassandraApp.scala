package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.demos

import com.datastax.spark.connector.{SomeColumns, _}
import pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.{CassandraIntegration, CassandraMethods}

import scala.util.Random

case class Foo(id: String, cnt: Long, word: String)

object ExampleCassandraApp extends CassandraIntegration with CassandraMethods {
  def main(args: Array[String]) {
    val sc = initialize(sparkHost = args.headOption.getOrElse(throw new IllegalArgumentException("provide spark master as first arument")))
    val keyspace = "test"
    val table = "test_table"
    createNamespace(keyspace, sc.sc.getConf)
    dropTable(keyspace, table, sc.sc.getConf)
    createTable(keyspace, table, "id" -> "text", List("word" -> "text", "cnt" -> "int"), sc.sc.getConf)
    val data = 1 to 100 map { i =>
      Foo(s"id$i", Random.nextLong(), Random.alphanumeric.take(10).mkString)
    }
    sc.sc.parallelize(data).saveToCassandra(keyspace, table, SomeColumns("id", "cnt", "word"))
    sc.sc.stop()
  }
}
