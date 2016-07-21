package org.apache.activemq.book.ch7.spring;

import org.apache.activemq.broker.BrokerService;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;

/**
 * Spring操作消息队列的入口程序
 */
public class SpringClient {

	public static void main(String[] args) throws Exception {
		//开启Broker
		/*BrokerService broker = new BrokerService();
		broker.addConnector("tcp://localhost:61616");
		broker.setPersistent(false);
		broker.start();*/
		
		//创建Spring上下文环境
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("src/main/resources/org/apache/activemq/book/ch7/spring-client.xml");
		//获取生产者对象
		SpringPublisher publisher = (SpringPublisher)context.getBean("stockPublisher");
		publisher.start();
	}

}
