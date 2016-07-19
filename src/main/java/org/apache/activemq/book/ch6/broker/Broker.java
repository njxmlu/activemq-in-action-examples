package org.apache.activemq.book.ch6.broker;

import java.util.ArrayList;
import java.util.List;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.JaasAuthenticationPlugin;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
/**
 *  代码的方式启动Broker 
 *	并加入身份认证插件
 */
public class Broker {

	public static void main(String[] args) throws Exception {
		//创建Broker
		BrokerService broker = new BrokerService();
		//给Broker设置名字，如果只启动一个broker可以不用设置名字
		broker.setBrokerName("localhost");
		//设置数据保存路径，这一句看起来有点突兀，因为没有持久化设置
		broker.setDataDirectory("data/");
		
		/*
		 * 创建认证插件
		 * AMQ将认证和授权模块配置成了可插拔式的插件形式
		 */
		SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();
		//创建用户集合，并添加到认证插件当中
		List<AuthenticationUser> users = new ArrayList<AuthenticationUser>();
		users.add(new AuthenticationUser("admin", "password", "admins,publishers,consumers"));
		users.add(new AuthenticationUser("publisher", "password", "publishers,consumers"));
		users.add(new AuthenticationUser("consumer", "password", "consumers"));
		users.add(new AuthenticationUser("guest", "password", "guests"));
		authentication.setUsers(users);
		//为broker设置认证插件
		broker.setPlugins(new BrokerPlugin[]{authentication});
		
		/*JaasAuthenticationPlugin jaas = new JaasAuthenticationPlugin();
		jaas.setConfiguration("src/main/resources/org/apache/activemq/book/ch5/login.config");
		broker.setPlugins(new BrokerPlugin[]{jaas});*/		
		
		broker.addConnector("tcp://localhost:61616");
		//开启broker
		broker.start();
		System.out.println("broker成功开启...");
	}

}
