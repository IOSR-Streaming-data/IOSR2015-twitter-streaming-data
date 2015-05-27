package pl.edu.agh.iosr.iosr2015.data.streaming.twitter

import com.datastax.spark.connector.SparkContextFunctions
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector._
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.CassandraIntegration
import pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.demos.ExampleCassandraApp._
import pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.demos.Foo
import pl.edu.agh.iosr.iosr2015.data.streaming.twitter.tfidf.{DocumentDB, Corpus, Document, DocumentPreprocessor}

import scala.util.Random
import scala.collection.JavaConversions._


object Main {

  val ctx = new CassandraIntegration {}

  def main(args: Array[String]) {
    if (args.length < 4) {
      System.err.println("Usage: Main <consumer key> <consumer secret> " +
        "<access token> <access token secret> [<filters>]")
      System.exit(1)
    }

    Logging.setStreamingLogLevels()

    val corpus = new Corpus

    val Array(consumerKey, consumerSecret, accessToken, accessTokenSecret) = args.take(4)
    val filters = args.takeRight(args.length - 4)

    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

    val sparkConf = new SparkConf().setAppName("IOSR Twitter Streaming Data")
    val ssc = new StreamingContext(sparkConf, Seconds(10))
    val stream = TwitterUtils.createStream(ssc, None, filters)
    implicit object Preprocessor extends DocumentPreprocessor

    val tweets: DStream[Document] = stream.map(status => Document(status.getText))

    val scf = new SparkContextFunctions(new SparkContext(sparkConf))
    val space = "testkeyspace"
    val table = "testtable"
    createNamespace(space, sparkConf)
    dropTable(space, table, sparkConf)
    createTable(space, table, "document_id" :: Nil, List("document_text" -> "text"), sparkConf)

    tweets.foreachRDD(rdd => {
      if (rdd.count() != 0) {
        rdd.collect().foreach { doc: Document =>
          println("new tweet: " + doc)
          corpus :+ doc

          val data = corpus.documents.map({ d =>
            DocumentDB(Random.nextInt(),d.text)
          })
          scf.sc.parallelize(data).saveToCassandra(space, table)

          CassandraConnector(sparkConf).withSessionDo { session =>
            printf("cassandra_counter: " + session.execute(s"select * from $space.$table;").iterator().toList.length + "\b")
          }
        }
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }
}