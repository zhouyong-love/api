package com.cloudok.message.queue;

import com.cloudok.message.vo.ChannelMessage;

/**
 * 消息队列
 * @author xiazhijian
 *
 */
public interface MessageQueue {

	/**
	 * 发布消息
	 * @param message
	 */
	void publish(ChannelMessage message);
	
}
