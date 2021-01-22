package com.cloudok.base.channel.ii;

import org.springframework.stereotype.Component;

import com.cloudok.base.message.channel.ChannelIoAdapter;
import com.cloudok.base.message.vo.ChannelMessage;
import com.cloudok.base.message.vo.MessageVO;
import com.cloudok.security.User;

@Component("iiMessageHandler")
public class IIHandler implements ChannelIoAdapter {

	@Override
	public String write(ChannelMessage channelMessage) {
		return "";
	}

	@Override
	public boolean isSuccess(MessageVO message) {
		return true;
	}

	@Override
	public String getReceviceByUser(User user) {
		return String.valueOf(user.getId());
	}

	@Override
	public boolean isSuccess(String channelResponse) {
		return true;
	}

}
