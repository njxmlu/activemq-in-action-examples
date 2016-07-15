package org.apache.activemq.book.ch3.portfolio;

import java.text.DecimalFormat;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
/**
 * 消息监听器
 * @author gczhang
 */
public class Listener implements MessageListener {
	
	/**
	 * 当queue或topic中有消息进入，则会触发这个方法
	 */
	public void onMessage(Message message) {
		try {
			/*
			 * 将消息转换成Map类型的消息，之所以能够转化是因为发布者发布的消息类型就是Map的
			 * 常见的Message类型有：
			 * 	  Map
			 * 	  Object
			 * 	  Text
			 * 	  Bytes
			 *    Stream
			 */
			MapMessage map = (MapMessage)message;
			
			/*
			 * 通过key依次获取message中所携带的信息 
			 */
			String stock = map.getString("stock");
			double price = map.getDouble("price");
			double offer = map.getDouble("offer");
			boolean up = map.getBoolean("up");
			DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
			System.out.println(stock + "\t" + df.format(price) + "\t" + df.format(offer) + "\t" + (up?"up":"down"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
