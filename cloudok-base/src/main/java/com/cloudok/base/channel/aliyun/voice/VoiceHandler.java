package com.cloudok.base.channel.aliyun.voice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.cloudok.base.message.channel.ChannelIoAdapter;
import com.cloudok.base.message.vo.ChannelMessage;
import com.cloudok.base.message.vo.MessageVO;
import com.cloudok.core.json.JSON;
import com.cloudok.security.User;
import com.cloudok.util.NumberUtil;

@Component("voiceMessageHandler")
public class VoiceHandler implements ChannelIoAdapter{

	@Autowired
	@Qualifier("voiceIAcsClient")
	private IAcsClient acsClient;
	
	@Override
	public String write(ChannelMessage channelParameters) {
		SingleCallByTtsRequest request = new SingleCallByTtsRequest();
		request.setCalledShowNumber(channelParameters.getChannelParameters().get("called-show-number"));
		request.setCalledNumber(channelParameters.getReceive());
		request.setTtsCode(channelParameters.getChannelParameters().get("tts-code"));
		request.setTtsParam(channelParameters.getParameters());
		request.setPlayTimes(NumberUtil.tryToInt(channelParameters.getChannelParameters().get("play-times"), 1));
		try {
			return JSON.toJSONString(acsClient.getAcsResponse(request));
		} catch (ClientException e) {
			e.printStackTrace();
			return "{}";
		}
	}

	@Override
	public boolean isSuccess(MessageVO message) {
		return true;
	}

	@Override
	public String getReceviceByUser(User user) {
		return user.getPhone();
	}

	@Override
	public boolean isSuccess(String channelResponse) {
		return true;
	}
}
