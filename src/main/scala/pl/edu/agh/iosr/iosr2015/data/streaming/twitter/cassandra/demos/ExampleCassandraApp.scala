package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.demos

import com.datastax.spark.connector.{SomeColumns, _}
import org.apache.spark.SparkConf
import pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.{CassandraIntegration, CassandraMethods}

import scala.util.Random

case class Foo(id: String, cnt: Long, word: String)

object ExampleCassandraApp extends CassandraIntegration with CassandraMethods {
  def main(args: Array[String]) {
    val conf: SparkConf = new SparkConf(true)
    val scf = initialize(conf, sparkHost = args.headOption.getOrElse(throw new IllegalArgumentException("provide spark master as first arument")))
    val keyspace = "test"
    val table = "test_table"
    createNamespace(keyspace, conf)
    dropTable(keyspace, table, conf)
    createTable(keyspace, table, "id" :: Nil, List("id" -> "text", "word" -> "text", "cnt" -> "int"), conf)
    val data = 1 to 100 map { i =>
      Foo(s"id$i", Random.nextLong(), Random.alphanumeric.take(10).mkString)
    }
    scf.sc.parallelize(data).saveToCassandra(keyspace, table, SomeColumns("id", "cnt", "word"))
    scf.sc.stop()
  }
}
