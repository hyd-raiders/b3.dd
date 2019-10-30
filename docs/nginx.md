
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