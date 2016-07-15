发布-订阅模式

JMS API

运行步骤：
	启动ActiveMQ;
	要先启动订阅者;
	然后再启动发布者;
注意：这里的先启动订阅者后启动发布者的顺序不能颠倒，否则将接收不到消息

1、启动订阅者（消费者）的maven指令：（当然，你完全可以不使用这个复杂的maven指令，而像运行一个普通java类去运行）
mvn exec:java -Dexec.mainClass=org.apache.activemq.book.ch3.portfolio.Consumer -Dexec.args="zgc hmy"
2、启动发布者的maven指令:
mvn exec:java -Dexec.mainClass=org.apache.activemq.book.ch3.portfolio.Publisher -Dexec.args="zgc hmy"

ps:如果是在eclipse当中运行的话，则不需要指明mvn