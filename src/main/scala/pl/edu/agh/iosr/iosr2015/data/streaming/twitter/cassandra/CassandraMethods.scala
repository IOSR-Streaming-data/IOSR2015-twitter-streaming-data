package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra

import com.datastax.driver.core.ResultSet
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.{SparkContext, SparkConf}

import scala.util.Try

trait CassandraMethods {
  def createNamespace(namespace: String, sc: SparkContext): ResultSet = createNamespace(namespace, sc.getConf)

  def createNamespace(namespace: String, conf: SparkConf): ResultSet = {
    CassandraConnector(conf).withSessionDo { session =>
      session.execute(s"CREATE KEYSPACE IF NOT EXISTS $namespace WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }")
    }
  }

  def createTable(namespace: String, table: String, primary: (String, String), columns: List[(String, String)], sc: SparkContext): ResultSet =
    createTable(namespace, table, primary, columns, sc.getConf)

  def createTable(namespace: String, table: String, primary: (String, String), columns: List[(String, String)], conf: SparkConf): ResultSet = {
    CassandraConnector(conf).withSessionDo { session =>
      val keys = columns.map { case (name, typ) => s"$name $typ" }.mkString(", ")
      session.execute(s"CREATE TABLE IF NOT EXISTS $namespace.$table (${primary._1} ${primary._2} PRIMARY KEY, $keys);")
    }
  }

  def dropTable(namespace: String, table: String, sc: SparkContext): ResultSet =
    dropTable(namespace, table, sc.getConf)

  def dropTable(namespace: String, table: String, conf: SparkConf): ResultSet = {
    CassandraConnector(conf).withSessionDo { session =>
      session.execute(s"DROP TABLE IF EXISTS $namespace.$table;")
    }
  }

  def truncate(space: String, table: String, conf: SparkConf): Try[ResultSet] = Try {
    CassandraConnector(conf).withSessionDo { session =>
      session.execute(s"TRUNCATE $space.$table;")
    }
  }

}

object CassandraMethods extends CassandraMethods