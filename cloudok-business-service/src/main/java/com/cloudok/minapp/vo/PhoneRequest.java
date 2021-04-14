package com.cloudok.minapp.vo;

import lombok.Data;

@Data
public class PhoneRequest {
	private String openId;
	private String sessionKey;
//	private String signature;
//	private String rawData;
	private String encryptedData;
	private String iv;
}
