package com.cloudok.base.channel.aliyun.voice;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ConfigurationProperties(prefix = "com.cloudok.channel.aliyun.voice")
public class AliyunVoiceProperties {

	private String accessKey;
	
	private String accessSecret;
	
	private String regionId; 
}
