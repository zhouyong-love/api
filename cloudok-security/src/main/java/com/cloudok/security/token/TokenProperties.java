package com.cloudok.security.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月29日 下午5:57:24
 */
@ConfigurationProperties(prefix = "security.token")
@Getter @Setter
public class TokenProperties {

	private  String secret;

	private  int expired;

	private  String signatureSecret;
	
	private int signatureExpired;
}
