package org.apache.activemq.book.ch3.portfolio;

import java.util.Hashtable;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
/**
 * 发布-订阅模式
 * 发布者，生成消息，发送到topic
 * @author gczhang
 *
 */
public class Publisher {
	
    protected int MAX_DELTA_PERCENT = 1;
    protected Map<String, Double> LAST_PRICES = new Hashtable<String, Double>();
    protected static int count = 10;
    protected static int total;
    //代理地址，即ActiveMQ所部署的主机地址
    protected static String brokerURL = "tcp://localhost:61616";
    protected static transient ConnectionFactory factory;
    protected transient Connection connection;
    protected transient Session session;
    protected transient MessageProducer producer;
    
    public Publisher() throws JMSException {
    	//创建连接工厂
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	//创建连接
    	connection = factory.createConnection();
    	try {
    		//开始连接
    		connection.start();
    	} catch (JMSException jmse) {
    		connection.close();
    		throw jmse;
    	}
    	/*
         * 通过连接创建session,session对象很重要，可以通过 其创建：①生产者、②消费者、③queue、④topic
         */
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //通过session创建生成者
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
                publisher.sendMessage(args);
            }
            total += count;
            System.out.println("Published '" + count + "' of '" + total + "' price messages");
            try {
              Thread.sleep(1000);
            } catch (InterruptedException x) {
            }
        }
        publisher.close();
    }
    
    /**
     * 发送消息
     * @param stocks 发往的topic名称
     * @throws JMSException
     */
    protected void sendMessage(String[] stocks) throws JMSException {
        int idx = 0;
        /*
         * 因为有多个topic，随机选择消息要发送至的topic
         */
        while (true) {
            idx = (int)Math.round(stocks.length * Math.random());
            if (idx < stocks.length) {
                break;
            }
        }
        String stock = stocks[idx];
        Destination destination = session.createTopic("STOCKS." + stock);
        //获取一条信息
        Message message = createStockMessage(stock, session);
        System.out.println("Sending: " + ((ActiveMQMapMessage)message).getContentMap() + " on destination: " + destination);
        //发送消息
        producer.send(destination, message);
    }
    
    
    /**
     * 创建Map类型的消息
     * 	    这里只需要知道这个方法就是创建一条消息，具体它的逻辑细节这里我们可以忽略。
     * @param stock
     * @param session
     * @return
     * @throws JMSException
     */
    protected Message createStockMessage(String stock, Session session) throws JMSException {
        Double value = LAST_PRICES.get(stock);
        if (value == null) {
            value = new Double(Math.random() * 100);
        }

        // lets mutate the value by some percentage
        double oldPrice = value.doubleValue();
        value = new Double(mutatePrice(oldPrice));
        LAST_PRICES.put(stock, value);
        double price = value.doubleValue();

        double offer = price * 1.001;

        boolean up = (price > oldPrice);
        //创造Map类型的Message，并往其中设置数据
		MapMessage message = session.createMapMessage();
		message.setString("stock", stock);
		message.setDouble("price", price);
		message.setDouble("offer", offer);
		message.setBoolean("up", up);
		return message;
    }

    protected double mutatePrice(double price) {
        double percentChange = (2 * Math.random() * MAX_DELTA_PERCENT) - MAX_DELTA_PERCENT;

        return price * (100 + percentChange) / 100;
    }

}
