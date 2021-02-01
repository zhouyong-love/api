package com.cloudok.base.channel.aliyun.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.cloudok.base.message.channel.ChannelIoAdapter;
import com.cloudok.base.message.vo.ChannelMessage;
import com.cloudok.base.message.vo.MessageVO;
import com.cloudok.security.User;

@Component("smsMessageHandler")
public class SMSHandler implements ChannelIoAdapter{

	private static final String DOMAIN = "dysmsapi.aliyuncs.com";

	private static final String VERSION = "2017-05-25";

	private static final String BATCHACTION = "SendSms";

	@Autowired
	private AliyunSMSProperties aliyunProperties;
	
	@Autowired
	@Qualifier("SMSIAcsClient")
	private IAcsClient acsClient;
	

	@Override
	public String write(ChannelMessage channelParameters) {
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain(DOMAIN);
		request.setSysVersion(VERSION);
		request.setSysAction(BATCHACTION);
		request.putQueryParameter("RegionId", aliyunProperties.getRegionId());
		request.putBodyParameter("PhoneNumbers", channelParameters.getReceive());
		request.putBodyParameter("SignName", channelParameters.getChannelParameters().get("sign-name"));
		request.putBodyParameter("TemplateCode", channelParameters.getChannelParameters().get("template-code"));
		request.putBodyParameter("TemplateParam", channelParameters.getParameters());
		try {
			return acsClient.getCommonResponse(request).getData();
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
