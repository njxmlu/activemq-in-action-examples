package org.apache.activemq.book.ch3.portfolio;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * 发布-订阅模式
 * 消费者，即订阅者，从topic上取出消息来消费
 * @author gczhang
 */
public class Consumer {
	
    private static String brokerURL = "tcp://localhost:61616";
    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    
    public Consumer() throws JMSException {
    	//创建连接工厂
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	//创建连接
    	connection = factory.createConnection();
    	//开始连接
        connection.start();
        /*
         * 通过连接创建session,session对象很重要，可以通过 其创建：①生产者、②消费者、③queue、④topic
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
    	Consumer consumer = new Consumer();
    	for (String stock : args) {
    		//通过目的地对象来指明具体的Topic,即消费者将会消费其中的Message
    		Destination destination = consumer.getSession().createTopic("STOCKS." + stock);
    		//创建消息消费者对象
    		MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);
    		//给消费者注册监听，一旦Topic中有消息时，会触发监听对象的方法
    		messageConsumer.setMessageListener(new Listener());
    	}
    }
	
	public Session getSession() {
		return session;
	}

} 
