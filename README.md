执行普通JAVA类（main class）的maven命令：
mvn exec:java  -Dexec.mainClass=org.apache.activemq.book.ch3.Consumer
-Dexec.args="ZGC HMY"

/*
         * 如果想读懂connection.createSession方法中参数的含义
         * 请参考：http://www.cnblogs.com/ywjy/p/5433891.html
         */

待看博文
http://www.cnblogs.com/ywjy/articles/5433680.html