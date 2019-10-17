# b3.dd
data driven


## design

![](./docs/dd.png)



## just do it

1.  [install kafka](https://github.com/apporoad/eploy/blob/master/docs/kafka.md)   test using [kafka tool](http://www.kafkatool.com/)
2.  install [hive](https://github.com/hyd-raiders/b2.xdo)

3. install  nginx with [kafka module](https://github.com/brg-liuwei/ngx_kafka_module)

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
   	location ~* /kafka/track {
   		kafka_topic test;
   	}
   	location ~* /kafka/get {
       		proxy_method POST;
          		proxy_set_body $args;
       		proxy_pass http://$host/kafka/track;
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

      ```
      #开启consumer
      /root/app/kafka_2.11-0.8.2.2/bin/kafka-console-consumer.sh --zookeeper zk-02:2181,zk-01:2181,zk-03:2181 --topic track --from-beginning
      
      # 浏览器测试 http://xxxxxxxxxxxx/kafka/get?p=1
      ```

   5. 士大夫

