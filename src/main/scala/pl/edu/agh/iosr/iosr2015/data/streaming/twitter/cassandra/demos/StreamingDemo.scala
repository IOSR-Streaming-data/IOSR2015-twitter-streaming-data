package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.demos

import com.datastax.spark.connector.SparkContextFunctions
import com.datastax.spark.connector.streaming._
import org.apache.spark.{SparkConf, Logging}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import pl.edu.agh.iosr.iosr2015.data.streaming.twitter.cassandra.{CassandraIntegration, CassandraMethods}

import scala.collection.mutable


case class WordCount(word: String, cnt: Int)

object StreamingDemo extends App with CassandraIntegration with CassandraMethods with Logging {

  val conf: SparkConf = new SparkConf(true)
  val csf: SparkContextFunctions = initialize(
    conf,
    sparkHost = args.headOption.getOrElse(throw new IllegalArgumentException("provide spark master as first argument")),
    appName = "StreamingDemo"
  )
  val sc = csf.sc
  val ssc = new StreamingContext(sc, Seconds(1))


  val lines = mutable.Queue[RDD[String]]()
  val dstream = ssc.queueStream(lines)
  lines += sc.makeRDD(Seq("To be or not to be.", "That is the question."))

  def badWords = List("is")

  //@formatter:off
  val words = dstream
    .map(x => {log.warn(s"working on $x"); x })
    .flatMap(_.split(" "))
    .map(_.stripPrefix(",").stripPrefix(".").toLowerCase)
    .map(x => {log.warn(s"working on $x"); x })
    .filter(x => !badWords.contains(x))
    .filter(x => !x.isEmpty)
  //@formatter:on

  val wordCounts: DStream[WordCount] = words.map(word => (word, 1)).reduceByKey(_ + _).map {
    case (word: String, count: Int) => WordCount(word, count)
  }

  val sortedWordCounts = wordCounts.transform(_.sortBy(_.cnt))

  val space: String = "wordcount"
  val table: String = "alamakota"
  createNamespace(space, conf)
  dropTable(space, table, conf)
  createTable(space, table, ("word", "text"), ("cnt", "int") :: Nil, conf)
  sortedWordCounts.saveToCassandra(space, table)
  ssc.start()
  ssc.awaitTermination()
}
