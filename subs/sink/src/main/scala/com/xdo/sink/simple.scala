package com.xdo.sink

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.kafka._

import java.util.HashMap


object simple {
  def main(args: Array[String]) {
    val ssc = new StreamingContext(sc, Seconds(5))

    val topicMap = Map("test" -> 1)

    //read data
    val lines = KafkaUtils.createStream(ssc,"hadoop:2181","testWordCountGroup",topicMap).map(_._2)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map( x => (x, 1)).reduceByKey(_ + _)
    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
