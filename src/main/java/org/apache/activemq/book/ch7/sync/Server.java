package org.apache.activemq.book.ch7.sync;

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
import org.apache.activemq.broker.BrokerService;
/**
 *  JMS实现请求和响应
 *  服务端，包含Consumer和Producer以及两个队列（请求队列和响应队列）
 */
public class Server implements MessageListener {
	
	private String brokerUrl = "tcp://0.0.0.0:61616";
	private String requestQueue = "requests";
	
	private BrokerService broker;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;
	

	public void start() throws Exception {
		createBroker();
		setupConsumer();
	}
	
	//创建Broker
	private void createBroker() throws Exception {
        broker = new BrokerService();
        //非持久化
        broker.setPersistent(false);
        //不适用JMX监控
        broker.setUseJmx(false);
        //设置连接器
        broker.addConnector(brokerUrl);
        //开启Broker
        broker.start();		
	}

	private void setupConsumer() throws JMSException {
		//创建连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		
		Connection connection;
		//创建连接
		connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//创建session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建队列目的地，用于请求
		Destination adminQueue = session.createQueue(requestQueue);
		//创建生产者
		producer = session.createProducer(null);
		//指定消息发送模式，非持久化
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		//创建消费者
		consumer = session.createConsumer(adminQueue);
		//为消费者指定监听
		consumer.setMessageListener(this);
	}
	
	//关闭资源
	public void stop() throws Exception {
		producer.close();
		consumer.close();
		session.close();
		broker.stop();
	}
	
	//监听消息，当服务器的消费者监听到消息的来临，就向消息指定的响应队列发送一个响应消息
	public void onMessage(Message message) {
        try {
        	//创建Text类型的响应消息
            TextMessage response = this.session.createTextMessage();
            if (message instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage) message;
                String messageText = txtMsg.getText();
                response.setText(handleRequest(messageText));
            }
            //指定CorrelationID,要和请求消息一致
            response.setJMSCorrelationID(message.getJMSCorrelationID());
            //服务端的生产者往响应队列发送响应消息
            producer.send(message.getJMSReplyTo(), response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
	}
	
	//指定响应消息的文本内容
	public String handleRequest(String messageText) {
		return "Response to '" + messageText + "'";
	}
	
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		server.start();
		
		System.out.println();
		System.out.println("Press any key to stop the server");
		System.out.println();
		
		System.in.read();
		
		server.stop();
	}

}
