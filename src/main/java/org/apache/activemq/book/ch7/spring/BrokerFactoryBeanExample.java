package org.apache.activemq.book.ch7.spring;

import org.apache.activemq.book.ch6.Publisher;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
/**
 * 通过配置文件启动broker
 * args: src/main/resources/org/apache/activemq/book/ch7/spring-1.0.xml
 *   或        src/main/resources/org/apache/activemq/book/ch2/activemq.xml
 *   	 src/main/resources/org/apache/activemq/book/ch4/brokerA.xml
 */
public class BrokerFactoryBeanExample {

	public static void main(String[] args) throws Exception {
    	if (args.length == 0) {
    		System.err.println("Please define a configuration file!");
    		return;
    	}
    	String config = args[0];
    	System.out.println("Starting broker with the following configuration: " + config);
		System.setProperty("activemq.base", System.getProperty("user.dir"));
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(config);
		
		//发送消息
		Publisher publisher = new Publisher();
		for (int i = 0; i < 100; i++) {
			publisher.sendMessage(new String[]{"JAVA", "IONA"});
		}
		
	}

}
