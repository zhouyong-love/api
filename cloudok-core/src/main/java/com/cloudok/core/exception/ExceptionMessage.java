package com.cloudok.core.exception;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:01:54
 */

@Getter
@Setter
public abstract class ExceptionMessage implements Serializable {

	private static final long serialVersionUID = -547904528497231913L;

	private int responseCode;
	
	private String code;

	private String message;

	public ExceptionMessage(String code, String message) {
		this.code = code;
		this.message = message;
		this.responseCode = 500;
	}
	
	public ExceptionMessage(int responseCode,String code, String message) {
		this.code = code;
		this.message = message;
		this.responseCode = responseCode;
	}
}
