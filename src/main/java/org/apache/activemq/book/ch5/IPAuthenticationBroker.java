package org.apache.activemq.book.ch5;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;
/**
 * 定义IP过滤的自定义插件
 * 	  虽然AMQ中的插件不单是用于安全方面，不过大多数时候我们只有在需要拓展安全机制的时候才会自定义插件
 * @author gczhang
 */
public class IPAuthenticationBroker extends BrokerFilter {

	List<String> allowedIPAddresses;
	Pattern pattern = Pattern.compile("^/([0-9\\.]*):(.*)");
	
	public IPAuthenticationBroker(Broker next, List<String> allowedIPAddresses) {
		super(next);
		this.allowedIPAddresses = allowedIPAddresses;
	}

	public void addConnection(ConnectionContext context,
			ConnectionInfo info) throws Exception {

		String remoteAddress = context.getConnection().getRemoteAddress();
		
		Matcher matcher = pattern.matcher(remoteAddress);
		if (matcher.matches()) {
			String ip = matcher.group(1);
			if (!allowedIPAddresses.contains(ip)) {
				throw new SecurityException("Connecting from IP address " + ip + " is not allowed");
			}
		} else {
			throw new SecurityException("Invalid remote address " + remoteAddress);
		}
		
		super.addConnection(context, info);
	}	
	
}
