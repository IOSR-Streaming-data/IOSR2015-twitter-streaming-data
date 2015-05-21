package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra

import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector
import org.scalatest.{Matchers, OneInstancePerTest, WordSpec}
import pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.demos.ExampleCassandraApp._
import pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.demos.Foo

import scala.collection.JavaConversions._
import scala.util.Random

class ExampleCassandra$Test extends WordSpec with OneInstancePerTest with Matchers {

  val ctx = new CassandraIntegration {}

  "it" should {
    "create tables" in {
      val (scf, conf) = ctx.initialize(sparkHost = "local[*]")
      val space = "testkeyspace"
      val table = "testtable"
      createNamespace(space, conf)
      dropTable(space, table, conf)
      createTable(space, table, "id" -> "text", List("word" -> "text", "cnt" -> "int"), conf)
      val data = 1 to 100 map { i =>
        Foo(s"id$i", Random.nextLong(), Random.alphanumeric.take(10).mkString)
      }
      scf.sc.parallelize(data).saveToCassandra(space, table)

      CassandraConnector(conf).withSessionDo { session =>
        session.execute(s"select * from $space.$table;").iterator().toList.length should equal(10)
      }
    }
  }
}
