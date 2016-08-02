package org.apache.activemq.book.ch7.xbean;

import org.apache.activemq.book.ch6.Publisher;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
/*
 * FileSystemXmlApplicationContext对象创建Broker
 * args: src/main/resources/org/apache/activemq/book/ch6/activemq-simple.xml
 */
public class XBeanBroker {

 public static void main(String[] args) throws Exception {
     if (args.length == 0) {
      System.err.println("Please define a configuration file!");
      return;
     }
     String config = args[0];
     System.out.println("Starting broker with the following configuration: " + config);
     System.setProperty("activemq.base", System.getProperty("user.dir"));
     //这句代码回创建Broker
     FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(config);             
     
     //创建消息发送者
     Publisher publisher = new Publisher();
     for (int i = 0; i < 100; i++) {                                 
    	 //发送消息
    	 publisher.sendMessage(new String[]{"JAVA", "IONA"});               
     }                                                                    
  
 }

}

