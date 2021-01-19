package com.cloudok.authority.vo;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月18日 下午12:11:41
 */
@Getter @Setter
public class LoginVO implements Serializable{

	private static final long serialVersionUID = -7226865309162826784L;

	private String userName;
	
	private String password;
	
	private String authType;
	
	private Map<String, String> parameters;
}
