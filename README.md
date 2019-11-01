<div align=center><img src="https://raw.githubusercontent.com/hyd-raiders/b3.dd/master/docs/logo.png"/></div>



# b3.dd

data driven


## design

![](./docs/dd.png)



## just do it 

[从零开始](./fromZero.md) 做dd

1. bury first

2. [install kafka](https://github.com/apporoad/eploy/blob/master/docs/kafka.md)   test using [kafka tool](http://www.kafkatool.com/)

3. install [hive](https://github.com/hyd-raiders/b2.xdo)

4. install  nginx with [kafka module](https://github.com/brg-liuwei/ngx_kafka_module)

   ```bash
   如果启动时报错：
   error while loading shared libraries: librdkafka.so.1: cannot open shared object file: No such file or directory
   
   表示找不到kafka.so.1的文件
   
   解决方法：
   
   echo "/usr/local/lib" >> /etc/ld.so.conf
   ldconfig   --需要执行的命令LDCONFIG
   ```

   ```nginx
   worker_processes  1;
   events {
       worker_connections  1024;
   }
   http {
       include       mime.types;
       default_type  application/octet-stream;
       sendfile        on;
       keepalive_timeout  65;
   
   	#ps+++++++++++++++++++++++++++++++++++++++++++++++++++++
       kafka;
       kafka_broker_list 192.168.10.158:19092;
       #ps+++++++++++++++++++++++++++++++++++++++++++++++++++++
       
       server {
           listen       80;
           server_name  localhost;
   
       #ps+++++++++++++++++++++++++++++++++++++++++++++++++++++
   	location ~* /bury/post {
   		kafka_topic bury;
   	}
   	location ~* /bury/get {
       		proxy_method POST;
          		proxy_set_body $args;
       		proxy_pass http://$host/bury/post;
       }
       #ps+++++++++++++++++++++++++++++++++++++++++++++++++++++
           location / {
               root   html;
               index  index.html index.htm;
           }
   
           error_page   500 502 503 504  /50x.html;
           location = /50x.html {
               root   html;
           }
       }
   }
   ```

   4. 测试

      ```bash
      #开启consumer
      #/root/app/kafka_2.11-0.8.2.2/bin/kafka-console-consumer.sh --zookeeper zk-02:2181,zk-01:2181,zk-03:2181 --topic track --from-beginning
      
      /opt/kafka_2.12-2.3.0/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic bury --from-beginning
      
      # 浏览器测试 http://xxxxxxxxxxxx/bury/get?p=1
      
      # try http://xxxxxxxxxxxxxxx:8123/track/?data=eyJldmVudCI6ICJ4eHgiLCJwcm9wZXJ0aWVzIjogeyIkb3MiOiAiV2luZG93cyIsIiRicm93c2VyIjogIkNocm9tZSIsIiRjdXJyZW50X3VybCI6ICJodHRwOi8vbG9jYWxob3N0OjgxMjMvaG9tZSIsIiRicm93c2VyX3ZlcnNpb24iOiA3MiwiJHNjcmVlbl9oZWlnaHQiOiA4NjQsIiRzY3JlZW5fd2lkdGgiOiAxNTM2LCJtcF9saWIiOiAid2ViIiwiJGxpYl92ZXJzaW9uIjogIjIuMjkuMSIsInRpbWUiOiAxNTcxODE2NDQwLjY1OCwiZGlzdGluY3RfaWQiOiAiMTZkZjM4YjYyNzg4MjAtMDYwMTI0ZjZlOThlNjItNTdiMTUzZC0xNDQwMDAtMTZkZjM4YjYyNzk3YzIiLCIkZGV2aWNlX2lkIjogIjE2ZGYzOGI2Mjc4ODIwLTA2MDEyNGY2ZTk4ZTYyLTU3YjE1M2QtMTQ0MDAwLTE2ZGYzOGI2Mjc5N2MyIiwiJGluaXRpYWxfcmVmZXJyZXIiOiAiaHR0cDovL2xvY2FsaG9zdDo4MTIzL2hvbWUiLCIkaW5pdGlhbF9yZWZlcnJpbmdfZG9tYWluIjogImxvY2FsaG9zdDo4MTIzIiwiaGVsbG8iOiAid29ybGQiLCJ0b2tlbiI6ICJ4eHh4In19&ip=1&_=1571816440658
      ```

   5. spark部署及开发

      [开发](./subs/spark.dev.md)

   6. 

