package org.apache.activemq.book.ch3.jobs;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
/**
 * 消息监听器
 * @author gczhang
 */
public class Listener implements MessageListener {

	private String job;
	
	public Listener(String job) {
		this.job = job;
	}
	
	/**
	 * 当关联的queue或topic中有消息进入，则会触发这个方法
	 */
	public void onMessage(Message message) {
		try {
			//do something here
			System.out.println(job + " id:" + ((ObjectMessage)message).getObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
