package org.apache.activemq.book.ch4;

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
 * 发布-订阅模型
 * 	    生产者，这里可以指定通信协议，通过制定一个运行参数
 * 	 ActiveMQ中支持的协议有：
 * 		 ①、TCP
 * 		 ②、NIO
 * 		 ③、UDP
 * 		 ④、SSL
 * 	     ⑤、HTTP(S)
 * 		 ⑥、VM	  
 * @author gczhang
 *
 */
public class Publisher {
	
    private int MAX_DELTA_PERCENT = 1;
    private Map<String, Double> LAST_PRICES = new Hashtable<String, Double>();
    private static int count = 10;
    private static int total;
    
    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private transient MessageProducer producer;
    
    public Publisher(String brokerURL) throws JMSException {
    	//创建连接工厂
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	//创建连接
    	connection = factory.createConnection();
    	//开始连接
        connection.start();
        //创建session
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
    	if (args.length == 0) {
    		System.err.println("Please define connection URI!");
    		return;
    	}

    	//用输入的第一个参数作为brokerURL
    	Publisher publisher = new Publisher(args[0]);


    	//extract topics from the rest of arguments
    	String[] topics = new String[args.length - 1];
    	System.arraycopy(args, 1, topics, 0, args.length - 1);
        while (total < 100) {
            for (int i = 0; i < count; i++) {
                publisher.sendMessage(topics);  //发送消息
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
        //创建目的地，简单点可以理解为Topic的名称
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
        //创建Map类型的消息
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
