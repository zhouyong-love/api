package com.cloudok.security.exception;

import com.cloudok.core.exception.ExceptionMessage;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月17日 下午11:18:29
 */
public class SecurityExceptionMessage extends ExceptionMessage{

	/**
	 * @param code
	 * @param message
	 */
	public SecurityExceptionMessage(String code, String message) {
		super(code, message);
	}

	private static final long serialVersionUID = 3668015154620889233L;

	public static final SecurityExceptionMessage PERMISSION_DENIED = new SecurityExceptionMessage("ACCESS.PERMISSION_DENIED", "Permission denied.");
	
	public static final SecurityExceptionMessage NO_AUTHENTICATION = new SecurityExceptionMessage("ACCESS.NO_AUTHENTICATION", "No authentication.");
	
	public static final SecurityExceptionMessage BAD_CERTIFICATE = new SecurityExceptionMessage("ACCESS.BAD_CERTIFICATE", "Bad certificate.");
	
	public static final SecurityExceptionMessage ACCESS_TOKEN_EXP = new SecurityExceptionMessage("ACCESS.ACCESS_TOKEN_EXP", "The token has expired.");
	
	public static final SecurityExceptionMessage ACCESS_INCORRECT_CERTIFICATE = new SecurityExceptionMessage("ACCESS.INCORRECT_CERTIFICATE", "Username or password incorrect.");

	public static final SecurityExceptionMessage ACCESS_ACCOUNT_FROZEN = new SecurityExceptionMessage("ACCESS.ACCOUNT_FROZEN", "Account number has been frozen.");

}
