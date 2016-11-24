package org.apache.activemq.book.ch5;

import java.util.List;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;

/**
 * 自定义插件还需要创建BrokerPlugin的实现类，用于安装插件到AMQ broker当中以及暴露出插件的配置
 */
public class IPAuthenticationPlugin implements BrokerPlugin {
	
	List<String> allowedIPAddresses;
	
	//实例化插件，返回一个带插件拦截链的Broker
	public Broker installPlugin(Broker broker) throws Exception {
		return new IPAuthenticationBroker(broker, allowedIPAddresses);
	}

	public List<String> getAllowedIPAddresses() {
		return allowedIPAddresses;
	}

	public void setAllowedIPAddresses(List<String> allowedIPAddresses) {
		this.allowedIPAddresses = allowedIPAddresses;
	}
	
	

}
