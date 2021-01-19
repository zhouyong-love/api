package com.cloudok.core.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:02:00
 */

@Getter@Setter
public class SystemException extends RuntimeException{

	private static final long serialVersionUID = 8013089097491789901L;

	private ExceptionMessage exceptionMessage;
	
	public SystemException(ExceptionMessage exceptionMessage) {
		super(exceptionMessage.getMessage());
		this.exceptionMessage=exceptionMessage;
	}
	
	public SystemException(String message) {
		super(message);
		this.exceptionMessage=CoreExceptionMessage.build(message);
	}
	
	public SystemException(String message,ExceptionMessage exceptionMessage) {
		super(message);
		this.exceptionMessage=exceptionMessage;
	}
	
	public SystemException(String code,String message) {
		super(message);
		this.exceptionMessage=CoreExceptionMessage.build(code,message);
	}
	
	public SystemException(Throwable exception) {
		super(exception);
		this.exceptionMessage=CoreExceptionMessage.build(exception);
	}
	
	public SystemException(ExceptionMessage exceptionMessage,Throwable exception) {
		super(exceptionMessage.getMessage(),exception);
		this.exceptionMessage=exceptionMessage;
	}
	
	public SystemException(String message,Throwable exception) {
		super(message,exception);
		this.exceptionMessage=CoreExceptionMessage.build(message);
	}
	
	public SystemException(String code,String message,Throwable exception) {
		super(message,exception);
		this.exceptionMessage=CoreExceptionMessage.build(code,message);
	}
	
}
