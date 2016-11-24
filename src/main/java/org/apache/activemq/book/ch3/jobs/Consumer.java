package org.apache.activemq.book.ch3.jobs;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * 点对点（P2P）模式
 * 	  
 * @author gczhang
 *
 */
public class Consumer {
	//指定代理的url
    private static String brokerURL = "tcp://127.0.0.1:61616";
    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    
    private String jobs[] = new String[]{"suspend", "delete"};
    
    public Consumer() throws JMSException {
    	//创建连接工厂
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	//创建连接
    	connection = factory.createConnection();
    	//开始连接
        connection.start();
        //通过连接创建会话对象
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
    	for (String job : consumer.jobs) {
    		//创建消息发往queue的名称
    		Destination destination = consumer.getSession().createQueue("JOBS." + job);
    		//创建消息消费者
    		MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);
    		//设置消息监听器
    		messageConsumer.setMessageListener(new Listener(job));
    	}
    }
	
	public Session getSession() {
		return session;
	}


}
