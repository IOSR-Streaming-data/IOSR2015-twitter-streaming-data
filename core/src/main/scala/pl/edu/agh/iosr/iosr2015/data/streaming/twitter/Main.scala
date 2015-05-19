package pl.edu.agh.iosr.iosr2015.data.streaming.twitter


import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import pl.edu.agh.iosr.iosr2015.data.streaming.twitter.tfidf.{DocumentPreprocessor, Corpus, Document}


object Main {

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

    tweets.foreachRDD(rdd => {
      if (rdd.count() != 0) {
        rdd.collect().foreach { doc: Document =>
          println("new tweet: " + doc)
          corpus :+ doc
        }
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }
}