package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.tfidf

/**
 * Created by Mateusz Antkiewicz
 */
class Corpus {
  var documents: List[Document] = List()
  val df = scala.collection.mutable.Map[String, Int]().withDefaultValue(0)

  def :+(document: Document) = {
    documents = documents :+ document
    updateDf(document)
  }

  def tfidf(document: Document, word: String) = {
    if (document.rank.contains(word)) {
      document.tf(word) * Math.log(documents.length / df(word))
    } else {
      0d
    }
  }

  private def updateDf(document: Document) = {
    document.words.foreach { word =>
      df(word) = df(word) + 1
    }
  }
}
