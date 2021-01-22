package com.cloudok.base.channel.aliyun.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ConfigurationProperties(prefix = "com.cloudok.channel.aliyun.sms")
public class AliyunSMSProperties {

	private String accessKey;
	
	private String accessSecret;
	
	private String regionId; 
}
