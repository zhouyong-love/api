package com.cloudok.core.vo;

import java.io.Serializable;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月15日 下午10:11:02
 */
public class ErrorMessage implements Serializable{

	private static final long serialVersionUID = -3327487612674605717L;

	private String errorCode;
	
	private String errorMessage;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
