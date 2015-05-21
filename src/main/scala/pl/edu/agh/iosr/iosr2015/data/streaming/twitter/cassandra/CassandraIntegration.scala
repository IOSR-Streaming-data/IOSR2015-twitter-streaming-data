package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra

import com.datastax.spark.connector._
import org.apache.spark.{SparkConf, SparkContext}


trait CassandraIntegration {

  def initialize(
                  cassandraHost: String = "127.0.0.1",
                  sparkHost: String = "127.0.0.1",
                  appName: String = "ExampleApplication"
                  ): (SparkContextFunctions, SparkConf) = {

    println(s"used:\n\tspark master: $sparkHost\n\tcassandra host:$cassandraHost\n\tin app:$appName")

    val conf = new SparkConf(true)
      .set("spark.cassandra.connection.host", cassandraHost)
      .setMaster(sparkHost)
      .setAppName(appName)

    /** Connect to the Spark cluster: */
    (new SparkContextFunctions(new SparkContext(conf)), conf)
  }
}
