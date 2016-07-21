package org.apache.activemq.book.ch7.sync;

import java.util.UUID;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
/**
 *  JMS实现请求和响应
 *  客户端，包含Consumer和Producer以及两个队列（请求队列和响应队列）
 */
public class Client implements MessageListener {

	private String brokerUrl = "tcp://0.0.0.0:61616";
	private String requestQueue = "requests";
	
	Connection connection;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;
	
	private Destination tempDest;
	
	public void start() throws JMSException {
		//创建连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				brokerUrl);
		//创建连接
		connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//创建session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建目的地（队列）
		Destination adminQueue = session.createQueue(requestQueue);
		//创建生产者
		producer = session.createProducer(adminQueue);
		//指定发送模式为非持久化，消息在Broker只存在于内存当中
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		//创建临时队列目的地，用作响应
		tempDest = session.createTemporaryQueue();
		//创建消费者
		consumer = session.createConsumer(tempDest);
		//为消费者设置监听
		consumer.setMessageListener(this);
	}
	
	//关闭资源
	public void stop() throws JMSException {
		producer.close();
		consumer.close();
		session.close();
		connection.close();
	}
	
	/**
	 * 发生消息
	 * 请求响应模式时，消息需要设置两个变量：JMSReplyTo和JMSCorrelationID
	 * @param request
	 * @throws JMSException
	 */
	public void request(String request) throws JMSException {
		System.out.println("Requesting: " + request);
		//创建Text类型的消息
		TextMessage txtMessage = session.createTextMessage();
		txtMessage.setText(request);
		
		//给消息设置回复的消息队列
		txtMessage.setJMSReplyTo(tempDest);

		String correlationId = UUID.randomUUID().toString();
		//给消息设置CorrelationID
		txtMessage.setJMSCorrelationID(correlationId);
		//发送消息
		this.producer.send(txtMessage);
	}
	
	//监听消息
	public void onMessage(Message message) {
		try {
			System.out.println("Received response for: "
					+ ((TextMessage) message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		client.start();
		int i = 0;
		while (i++ < 10) { //发送10个消息
			client.request("REQUEST-" + i);
		}
		Thread.sleep(3000); //睡3秒等待响应
		client.stop();
	}

}
