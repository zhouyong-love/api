package com.cloudok.base.channel.jpush;

import org.springframework.stereotype.Component;

import com.cloudok.base.message.channel.ChannelIoAdapter;
import com.cloudok.base.message.vo.ChannelMessage;
import com.cloudok.base.message.vo.MessageVO;
import com.cloudok.security.User;

@Component("jpushMessageHandler")
public class JPushHandle implements ChannelIoAdapter{


	@Override
	public boolean isSuccess(MessageVO message) {
		return false;
	}

	@Override
	public String getReceviceByUser(User user) {
		return null;
	}

	@Override
	public String write(ChannelMessage channelMessage) {
		return null;
	}

	@Override
	public boolean isSuccess(String channelResponse) {
		return true;
	}
	
}
