package org.apache.activemq.book.ch3.jobs;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * 点对点（P2P）模式
 * 	  生成者
 * @author gczhang
 *
 */
public class Publisher {
	//指定代理的url
    private static String brokerURL = "tcp://localhost:61616";
    private static transient ConnectionFactory factory;
    private transient Connection connection; 
    private transient Session session;
    private transient MessageProducer producer;
    
    private static int count = 10;
    private static int total;
    private static int id = 1000000;
    
    private String jobs[] = new String[]{"suspend", "delete"};
    
    public Publisher() throws JMSException {
    	//创建连接工厂
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	//创建连接
    	connection = factory.createConnection();
    	//开始连接
        connection.start();
        //通过连接创建会话对象
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建生产者
        producer = session.createProducer(null);
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
    	Publisher publisher = new Publisher();
        while (total < 100) {
            for (int i = 0; i < count; i++) {
            	//循环发送消息
                publisher.sendMessage();
            }
            total += count;
            System.out.println("Published '" + count + "' of '" + total + "' job messages");
            try {
              Thread.sleep(1000);
            } catch (InterruptedException x) {
            }
          }
        //最后记得关闭连接
        publisher.close();

	}
	
	/**
	 * 发送消息
	 * @throws JMSException
	 */
    public void sendMessage() throws JMSException {
        int idx = 0;
        /*
         * 随机指定目的地
         */
        while (true) {
            idx = (int)Math.round(jobs.length * Math.random());
            if (idx < jobs.length) {
                break;
            }
        }
        String job = jobs[idx];
        //创建queue目的地
        Destination destination = session.createQueue("JOBS." + job);
        //创建一个Object类型的消息
        Message message = session.createObjectMessage(id++);
        System.out.println("Sending: id: " + ((ObjectMessage)message).getObject() + " on queue: " + destination);
        //生产者发送消息
        producer.send(destination, message);
    }	

}
