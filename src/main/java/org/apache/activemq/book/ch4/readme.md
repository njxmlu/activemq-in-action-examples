##启动嵌入式Broker（纯代码方式）
	回顾一下我们是如何启动ActiveMQ的，定位到ActiveMQ的安装目录，在bin\win64目录中，双击activemq.bat，即可启动ActiveMQ，其实这里是为我们启动了Broker（翻译成代理吧，这里实在不好说清Broker是什么东东，姑且理解为ActiveMQ服务吧），只是这种方法我们可以在浏览器进入到ActiveMQ的控制台（http://127.0.0.1:8161）。
	除了上面这种方式启动Broker之外，我们也可以在代码中启动Broker。不过貌似这种方式启动，我们无法进入到ActiveMQde控制台
	
##相关API
	BrokerService
	
##消息存储
	我们的消息从生产者发送到Provider之后，消息在Provider当中是如何存储的呢？ActiveMQ提供了如下几种存储方式：
	1、内存存储（对应非持久化，只要ActiveMQ主机宕机之后，消息就会消失）；
	2、KahaDB，这是一个文件型数据库,KahaDB是默认的持久化（注意，持久化）存储方式；
	3、AMQ，存储在文件当中；
	4、JDBC，存储在关系型数据库当中；
	
	其中，KahaDB和AMQ的文件，存放在{AMQ_HOME}/data目录下。
	