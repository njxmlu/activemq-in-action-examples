package org.apache.activemq.book.ch7.xbean;

import java.util.ArrayList;
import java.util.List;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
/**
 * 代码的方式启动Broker
 */
public class Broker {

	public static void main(String[] args) throws Exception {
		BrokerService broker = new BrokerService();
		broker.setBrokerName("myBroker");
		//设置Message持久化文件保存的的目录，代码运行的时候会在当前目录下创建data/myBroker目录
		broker.setDataDirectory("data/");
		
		//身份认证插件
		SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();
		
		List<AuthenticationUser> users = new ArrayList<AuthenticationUser>();
		users.add(new AuthenticationUser("admin", "password", "admins,publishers,consumers"));
		users.add(new AuthenticationUser("publisher", "password", "publishers,consumers"));
		users.add(new AuthenticationUser("consumer", "password", "consumers"));
		users.add(new AuthenticationUser("guest", "password", "guests"));
		//设置受信任的user
		authentication.setUsers(users);
		//broker设置身份认证插件
		broker.setPlugins(new BrokerPlugin[]{authentication});
		
		/*JaasAuthenticationPlugin jaas = new JaasAuthenticationPlugin();
		jaas.setConfiguration("src/main/resources/org/apache/activemq/book/ch5/login.config");
		broker.setPlugins(new BrokerPlugin[]{jaas});*/		
		
		broker.addConnector("tcp://localhost:61616");
		
		broker.start();
		
		System.out.println();
		System.out.println("Press any key to stop the broker");
		System.out.println();
		
		System.in.read();
	}

}
