package pl.edu.agh.iosr.iosr2015.data.streaming.twitter

import org.apache.log4j.{Level, Logger}

object Logging {
  def setStreamingLogLevels() {
    val log4jInitialized = Logger.getRootLogger.getAllAppenders.hasMoreElements
    if (!log4jInitialized) {
      Logger.getRootLogger.setLevel(Level.INFO)
    }
  }
}