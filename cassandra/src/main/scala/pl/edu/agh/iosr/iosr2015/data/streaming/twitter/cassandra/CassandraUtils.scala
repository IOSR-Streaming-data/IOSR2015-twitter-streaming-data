package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra

import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.SparkConf

import scala.util.Try

trait CassandraUtils {
  def createNamespace(namespace: String, conf: SparkConf) = {
    CassandraConnector(conf).withSessionDo { session =>
      session.execute(s"CREATE KEYSPACE IF NOT EXISTS $namespace WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }")
    }
  }

  def createTable(namespace: String, table: String, primary: (String, String), columns: List[(String, String)], conf: SparkConf) = {
    CassandraConnector(conf).withSessionDo { session =>
      val keys = columns.map { case (name, typ) => s"$name $typ" }.mkString(", ")
      session.execute(s"CREATE TABLE IF NOT EXISTS $namespace.$table (${primary._1} ${primary._2} PRIMARY KEY, $keys);")
    }
  }

  def truncate(space: String, table: String, conf: SparkConf) = Try {
    CassandraConnector(conf).withSessionDo { session =>
      session.execute(s"TRUNCATE $space.$table;")
    }
  }
}

object CassandraUtils extends CassandraUtils