package com.cloudok.base.message.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloudok.base.message.channel.ChannelHandler;
import com.cloudok.base.message.vo.ChannelMessage;

/**
 * 默认实现， 不走队列直接写
 * @author xiazhijian
 *
 */
@Component
public class LocalMessageQueue implements MessageQueue{

	@Autowired
	private ChannelHandler channelHandler;
	
	@Override
	public void publish(ChannelMessage message) {
		channelHandler.write(message);
	}
}
