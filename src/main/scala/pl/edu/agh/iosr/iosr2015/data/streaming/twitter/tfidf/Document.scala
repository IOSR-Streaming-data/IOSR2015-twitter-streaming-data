package pl.edu.agh.iosr.iosr2015.data.streaming.twitter.tfidf


/**
 * Created by Mateusz Antkiewicz
 */

case class DocumentDB(document_id: Int, document_text: String)

case class Document(text: String)(implicit preprocessor: DocumentPreprocessor) {
  val words = preprocessor.toWords(text)
  val rank = preprocessor.toRank(words)

  def tf(word: String) = rank.getOrElse(word, 0)

  override def toString = s"Document($text)"
}
