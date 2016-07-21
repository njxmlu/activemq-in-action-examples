package org.apache.activemq.book.ch7.spring;

import java.util.HashMap;

import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;

/**
 * 通过Spring来发送消息
 * 借助JmsTemplate类
 *
 */
public class SpringPublisher {
	
    private JmsTemplate template;
    private int count = 10;
    private int total;
    private Destination[] destinations;
    //Map将目的地和消息创建者联系在一起
    private HashMap<Destination,StockMessageCreator> creators = new HashMap<Destination,StockMessageCreator>(); 
    
    //开始发送消息
    public void start() {
        while (total < 1000) {
            for (int i = 0; i < count; i++) {
                sendMessage();
            }
            total += count;
            System.out.println("Published '" + count + "' of '" + total + "' price messages");
            try {
              Thread.sleep(1000);
            } catch (InterruptedException x) {
            }
          }	
    }
    
    //发送消息
    protected void sendMessage() {
        int idx = 0;
        //随机选择目的地
        while (true) {
            idx = (int)Math.round(destinations.length * Math.random());
            if (idx < destinations.length) {
                break;
            }
        }
        Destination destination = destinations[idx];
        //JmsTemplate类发送消息
        template.send(destination, getStockMessageCreator(destination));
    }
    
    //根据目的地获取消息创建者
    private StockMessageCreator getStockMessageCreator(Destination dest) {
    	if (creators.containsKey(dest)) {
    		return creators.get(dest);
    	} else {
    		StockMessageCreator creator = new StockMessageCreator(dest);
    		creators.put(dest, creator);
    		return creator;
    	}
    }

	public JmsTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public Destination[] getDestinations() {
		return destinations;
	}

	public void setDestinations(Destination[] destinations) {
		this.destinations = destinations;
	}
    

	
}
