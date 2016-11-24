package org.apache.activemq.book.ch5;

import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.Message;
import org.apache.activemq.security.MessageAuthorizationPolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 消息级别的授权，判断消息是否可以消费
 * 
 * 消息级别的认证可以通过实现MessageAuthorizationPolicy接口来实现
 *
 */
public class AuthorizationPolicy implements
		MessageAuthorizationPolicy {
	
    private static final Log LOG = LogFactory.getLog(AuthorizationPolicy.class);
    
    //是否允许消费消息，如果返回true则允许消息被消费
	public boolean isAllowedToConsume(ConnectionContext context, Message message) {
		LOG.info(context.getConnection().getRemoteAddress());
		if (context.getConnection().getRemoteAddress().startsWith("/127.0.0.1")) {
			return true;
		} else {
			return false;
		}
	}

}
