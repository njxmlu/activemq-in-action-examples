package org.apache.activemq.book.ch6.broker;

import java.net.URI;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
/**
 *  代码的方式启动Broker 
 *	并加入身份认证插件
 */
public class Factory {

	public static void main(String[] args) throws Exception {
		System.setProperty("activemq.base", System.getProperty("user.dir"));
		System.out.println(System.getProperty("activemq.base"));
		//通过BrokerFactory创建BrokerService
		BrokerService broker = BrokerFactory.createBroker(new URI("xbean:src/main/resources/org/apache/activemq/book/ch5/activemq-simple.xml"));
		//开启Broker
		broker.start();
		
		System.out.println("start broker success...");
	}

}
