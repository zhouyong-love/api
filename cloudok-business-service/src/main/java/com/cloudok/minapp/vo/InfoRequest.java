package com.cloudok.minapp.vo;

import lombok.Data;

@Data
public class InfoRequest {
//	private String openId;
//	private String nickName;
//	private String avatarUrl;
	private String sessionKey;
	private String signature;
	private String rawData;
	private String encryptedData;
	private String iv;

}
