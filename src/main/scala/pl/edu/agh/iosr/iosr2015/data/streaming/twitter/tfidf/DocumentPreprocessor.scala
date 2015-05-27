package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.tfidf

/**
 * Created by Mateusz Antkiewicz
 */
trait DocumentPreprocessor {
  def toWords(text: String): List[String] =
    text
      .replaceAll("[^\\w]+", "")
      .toLowerCase
      .split("\\s+")
      .filter(!_.isEmpty)
      .toList


  def toRank(words: List[String]): Map[String, Int] =
    words
      .groupBy(identity)
      .mapValues(_.size)
      .map(identity)
}