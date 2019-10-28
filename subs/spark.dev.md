### 环境安装

1. jdk1.8 及环境变量
2. [scala 2.11.6](http://www.scala-lang.org/download/2.11.6.html) 注：默认安装选项会自动配置环境变量,安装路径不能有空格。

3. ide IntelliJ IDEA

在idea中，打开Files->settings选择Plugins,输入scala查看： 确定scala插件已经安装

### 需要解决sbt慢的问题

[sbt slow](https://www.cnblogs.com/30go/p/7909630.html)

### spark shell

```bash
# jars put in /home/sparkjars/

su hdfs
ls /home/sparkjars/
spark2-shell --jars /home/sparkjars/fastjson-1.2.47.jar,/home/sparkjars/spark-streaming-kafka-0-10_2.11-2.2.0.cloudera2.jar 

```

```scala

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe


val kafkaParams = Map[String, Object](
  "bootstrap.servers" -> "192.168.10.158:19092",
  "key.deserializer" -> classOf[StringDeserializer],
  "value.deserializer" -> classOf[StringDeserializer],
  "group.id" -> "use_a_separate_group_id_for_each_stream",
  "auto.offset.reset" -> "latest",
  "enable.auto.commit" -> (false: java.lang.Boolean)
)

val topics = Array("bury")
val stream = KafkaUtils.createDirectStream[String, String](
  streamingContext,
  PreferConsistent,
  Subscribe[String, String](topics, kafkaParams)
)


```

```java
val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount").set("spark.driver.allowMultipleContexts", "true")
val ssc = new StreamingContext(conf, Seconds(1))
    
val sparkConf = new SparkConf().setAppName("Twitter Streaming").setMaster("local[4]").set("spark.driver.allowMultipleContexts", "true")
```



### step by step 开发

[spark streaming](./sparkStreaming.md)









