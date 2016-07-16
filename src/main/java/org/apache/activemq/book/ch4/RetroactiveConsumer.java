package org.apache.activemq.book.ch4;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.book.ch3.portfolio.Listener;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

/**
 * 回溯消费者
 * 可回溯类型的订阅者，就可以获取那些原本不属于自己但broker上还保存的旧消息
 * （订阅者还未实例化之前，发布者发布的消息???? 这条貌似无法验证）
 *
 */
public class RetroactiveConsumer {
	
	public static void main(String[] args) throws JMSException {
		new RetroactiveConsumer().createRetroactiveConsumer();
	}
    
    public void createRetroactiveConsumer() throws JMSException  {
        
        ConnectionFactory fac = new ActiveMQConnectionFactory();        
        Connection connection = fac.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //设置为回溯消费者，只对topic有效
        Topic topic = session.createTopic("TEST.TOPIC?consumer.retroactive=true");
        MessageConsumer consumer = session.createConsumer(topic);
      //设置监听
        consumer.setMessageListener(new Listener());
    }
}
