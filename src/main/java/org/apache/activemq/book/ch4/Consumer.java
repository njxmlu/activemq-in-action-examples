package org.apache.activemq.book.ch4;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.book.ch3.portfolio.Listener;
/*
 * 发布-订阅模型
* 	    消费者，这里可以指定通信协议，通过制定一个运行参数
* 	 ActiveMQ中支持的协议有：
* 		 ①、TCP
* 		 ②、NIO
* 		 ③、UDP
* 		 ④、SSL
* 	     ⑤、HTTP(S)
* 		 ⑥、VM	
*/
public class Consumer {

    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    
    public Consumer(String brokerURL) throws JMSException {
    	//创建连接工厂
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	//创建连接
    	connection = factory.createConnection();
    	//开始连接
        connection.start();
        /*
         * 如果想读懂createSession方法中参数的含义
         * 请参考：http://www.cnblogs.com/ywjy/p/5433891.html
         */
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    
    /**
     * 关闭连接
     * @throws JMSException
     */
    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }    
    
    public static void main(String[] args) throws JMSException {
    	if (args.length == 0) {
    		System.err.println("Please define connection URI!");
    		return;
    	}
    	
    	//用输入的第一个参数作为brokerURL
    	Consumer consumer = new Consumer(args[0]);
    	
    	String[] topics = new String[args.length - 1];
    	//这段代码的效果为将args数组中的下标为0的元素去掉
    	System.arraycopy(args, 1, topics, 0, args.length - 1);
    	for (String stock : topics) {
    		//创建目的地
    		Destination destination = consumer.getSession().createTopic("STOCKS." + stock);
    		//创建消息者
    		MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);
    		//设置监听
    		messageConsumer.setMessageListener(new Listener());
    	}
    }
	
	public Session getSession() {
		return session;
	}

}
