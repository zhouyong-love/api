package com.cloudok.base.message.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloudok.base.message.service.MessageDetailsService;
import com.cloudok.base.message.vo.ChannelMessage;
import com.cloudok.base.message.vo.MessageDetailsVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.security.User;


/**
 * 消息真正处理
 * @author xiazhijian
 *
 */
@Component
public class ChannelHandler {

	@Autowired
	private MessageDetailsService messageDetailService;
	
	/**
	 * 将消息写到渠道中去
	 * @param message
	 */
	public void write(ChannelMessage message) {
		ChannelIoAdapter channel = SpringApplicationContext.getBean(message.getChannel()+"MessageHandler", ChannelIoAdapter.class);
		String channelResponse = channel.write(message);
		MessageDetailsVO detailVO=new MessageDetailsVO();
		detailVO.setId(message.getId());
		detailVO.setCallMessage(channelResponse);
		detailVO.setStatus(channel.isSuccess(channelResponse)?1:99);
		messageDetailService.merge(detailVO);
	}
	
	/**
	 * 获取接收者
	 * @return
	 */
	public String getReceive(User user,String messageType) {
		return SpringApplicationContext.getBean(messageType+"MessageHandler", ChannelIoAdapter.class).getReceviceByUser(user);
	}
}
