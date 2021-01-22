package com.cloudok.base.channel.aliyun.voice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

@Configuration
@EnableConfigurationProperties(AliyunVoiceProperties.class)
public class AliyunVoiceConfigure {


	@Autowired
	private AliyunVoiceProperties aliyunProperties;

	@SuppressWarnings("deprecation")
	@Bean(name = "voiceIAcsClient")
	public IAcsClient getAcsClient() throws ClientException {
		DefaultProfile profile = DefaultProfile.getProfile(aliyunProperties.getRegionId(), aliyunProperties.getAccessKey(), aliyunProperties.getAccessSecret());
		DefaultProfile.addEndpoint(aliyunProperties.getRegionId(),aliyunProperties.getRegionId(), "Dyvmsapi", "dyvmsapi.aliyuncs.com");
		IAcsClient client = new DefaultAcsClient(profile);
		return client;
	}
}
