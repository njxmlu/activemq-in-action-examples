package org.apache.activemq.book.ch4;

import org.apache.activemq.broker.BrokerService;

/**
 * 代码的方式启动Broker
 * 消息保存在内存当中
 */
public class MemoryStoreEmbeddedBroker {
    
	public static void main(String[] args) throws Exception {
		new MemoryStoreEmbeddedBroker().createEmbeddedBroker();
	}
	
    public void createEmbeddedBroker() throws Exception {
        //创建BrokerService对象
        BrokerService broker = new BrokerService();
        //设置为使用内存来存储消息，这个值默认是true
        broker.setPersistent(false);
          
        //添加一个连接器
        broker.addConnector("tcp://localhost:61616");

        //开启代理
        broker.start();
        
        System.out.println("broker启动成功");
      }
}
