package com.xdo.sink


import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import java.util.HashMap

import org.apache.spark._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe


object simple {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local[2]").setAppName("SparkStreamingTest")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(5))
    //nc -l -p 7777
    val lines = ssc.socketTextStream("localhost",7777)

    val wordsDstream= lines.flatMap(_.split(" "))
    //6、每一个单词计为1
    val wordAndOneDstream = wordsDstream.map((_,1))
    //7、相同单词出现的次数累加
    val result= wordAndOneDstream.reduceByKey(_+_)
    //8、打印结果
    result.print()

    ssc.start()


    //该方法为一直阻塞在这里，等待程序结束
    ssc.awaitTermination()







//    val conf = new SparkConf().setAppName("singleSink").setMaster("local[n]")
//    val ssc= new StreamingContext(conf,Seconds(10))
//
//    //kafka parames
//    val kafkaParams = Map[String, Object](
//      "bootstrap.servers" -> "192.168.10.158:19092",
//      "key.deserializer" -> classOf[StringDeserializer],
//      "value.deserializer" -> classOf[StringDeserializer],
//      "group.id" -> "use_a_separate_group_id_for_each_stream",
//      "auto.offset.reset" -> "latest",
//      "enable.auto.commit" -> (false: java.lang.Boolean)
//    )
//    val topics = Array("bury")
//    val stream = KafkaUtils.createDirectStream(
//      ssc,
//      PreferConsistent,
//      Subscribe[String, String](topics, kafkaParams)
//    )
//    stream.start()
  }
}
