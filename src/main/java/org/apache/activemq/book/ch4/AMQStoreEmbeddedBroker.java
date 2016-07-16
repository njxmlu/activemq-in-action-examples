package org.apache.activemq.book.ch4;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.amq.AMQPersistenceAdapterFactory;
/**
 * 代码的方式启动Broker
 * 采用AMQ的方式存储消息
 */
public class AMQStoreEmbeddedBroker {
	
	public static void main(String[] args) throws Exception {
		new AMQStoreEmbeddedBroker().createEmbeddedBroker();
	}
    
    public void createEmbeddedBroker() throws Exception {
        //创建BrokerService对象
        BrokerService broker = new BrokerService();
        //创建AMQ存储方式的适配器工厂
        AMQPersistenceAdapterFactory persistenceFactory = new AMQPersistenceAdapterFactory();
        
        //设定当个存储文件的大小，超出这个大小的话再新建一个文件出来
        persistenceFactory.setMaxFileLength(1024*16);
        //是否持久化日志索引，如果设置为false，则索引存放在内存当中
        persistenceFactory.setPersistentIndex(true);
        //设定存储方式
        broker.setPersistenceFactory(persistenceFactory);
        //添加一个连接器
        broker.addConnector("tcp://localhost:61616");
        //开启Broker
        broker.start();
    }
}
