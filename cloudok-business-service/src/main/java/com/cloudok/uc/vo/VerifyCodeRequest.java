package com.cloudok.uc.vo;

public class VerifyCodeRequest {
	private String country;
	private String key;// email or phone num
	private String type; // email or sms
	private String module; // login, signup, forgot, bind

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

}
