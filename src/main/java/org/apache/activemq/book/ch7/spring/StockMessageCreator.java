package org.apache.activemq.book.ch7.spring;

import java.util.Hashtable;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.jms.core.MessageCreator;

/**
 * Spring的方式创建消息
 * 使用Spring来发送消息，最普遍的方式是实现MessageCreator接口来创建消息，然后再通过JmsTemplate类的
 * send()发送消息
 *
 */
public class StockMessageCreator implements MessageCreator {

	private int MAX_DELTA_PERCENT = 1;
	private Map<Destination, Double> LAST_PRICES = new Hashtable<Destination, Double>();

	Destination stock;

	public StockMessageCreator(Destination stock) {
		this.stock = stock;
	}
	
	//实现MessageCreator接口需要实现这个方法
	public Message createMessage(Session session) throws JMSException {
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
		//创建Map类型的Message
		MapMessage message = session.createMapMessage();
		message.setString("stock", stock.toString());
		message.setDouble("price", price);
		message.setDouble("offer", offer);
		message.setBoolean("up", up);
		System.out.println("Sending: " + ((ActiveMQMapMessage)message).getContentMap() + " on destination: " + stock);
		return message;
	}
	
	//改变价格
	protected double mutatePrice(double price) {
		double percentChange = (2 * Math.random() * MAX_DELTA_PERCENT)
				- MAX_DELTA_PERCENT;

		return price * (100 + percentChange) / 100;
	}


}
