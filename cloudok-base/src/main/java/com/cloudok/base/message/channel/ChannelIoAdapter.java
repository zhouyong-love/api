package com.cloudok.base.message.channel;

import com.cloudok.base.message.vo.ChannelMessage;
import com.cloudok.base.message.vo.MessageVO;
import com.cloudok.security.User;


public interface ChannelIoAdapter {

	/**
	 * 发送消息
	 * @return
	 */
	String write(ChannelMessage channelMessage);
	
	/**
	 * 消息是否成功判断
	 * @param message
	 * @return
	 */
	boolean isSuccess(MessageVO message);
	
	/**
	 * 消息是否成功判断
	 * @param channelResponse
	 * @return
	 */
	boolean isSuccess(String channelResponse);
	
	/**
	 * 从用户信息中获取接收责
	 * @param user
	 * @return
	 */
	String getReceviceByUser(User user);
}
