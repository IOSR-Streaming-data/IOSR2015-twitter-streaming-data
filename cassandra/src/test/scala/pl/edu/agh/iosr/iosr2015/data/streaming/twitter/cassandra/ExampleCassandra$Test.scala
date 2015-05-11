package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra

import com.datastax.spark.connector.cql.CassandraConnector
import org.scalatest.{Matchers, OneInstancePerTest, WordSpec}
import scala.collection.JavaConversions._

class ExampleCassandra$Test extends WordSpec with OneInstancePerTest with Matchers{

  val ctx = new ExampleCassandra {}

  "it" should {
    "create tables" in {
      val (sc, space: String, table: String) = ctx.initialize()
      ctx.saveSomeData(sc,space,table)
      CassandraConnector(sc.sc.getConf).withSessionDo { session =>
        session.execute(s"select * from $space.$table").iterator().toList.length should equal(10)
      }
    }
  }
}
