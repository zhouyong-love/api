package com.cloudok.exception;

import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.ExceptionMessage;

public class CloudOKExceptionMessage extends ExceptionMessage{

	public CloudOKExceptionMessage(String code, String message) {
		super(code, message);
	}

	private static final long serialVersionUID = 8239091920220949891L;
	
	public static final String UNDEFINED_ERROR="COMMON.UNDEFINED_ERROR";


	public static final CloudOKExceptionMessage TEMPLATE_NOT_FOUND =new CloudOKExceptionMessage("BUS.TEMPLATE_NOT_FOUND", "Template not found.");
	
	public static final CloudOKExceptionMessage TEMPLATE_GENERATOR_ERROR =new CloudOKExceptionMessage("CORE.TEMPLATE_GENERATOR_ERROR", "Generator failed.");
	
	public static final CloudOKExceptionMessage DEVICE_NOT_EXISTS =new CloudOKExceptionMessage("BUS.DEVICE_NOT_EXISTS", "Device not exists.");
	
	public static final CloudOKExceptionMessage DEVICE_ALREADY_BIND_BY_OTHERS =new CloudOKExceptionMessage("BUS.DEVICE_ALREADY_BIND_BY_OTHERS", "Device already bind by others.");
	
	public static final CloudOKExceptionMessage DEVICE_NAME_EMPTY =new CloudOKExceptionMessage("BUS.DEVICE_NAME_EMPTY", "Please enter device name.");
	
	public static final CloudOKExceptionMessage DEVICE_ALREADY_BIND =new CloudOKExceptionMessage("BUS.DEVICE_ALREADY_BIND", "Device already bind.");
	
	public static final CloudOKExceptionMessage VERIFY_CODE_SEND_ERROR =new CloudOKExceptionMessage("BUS.VERIFY_CODE_SEND_ERROR", "Verify code send failed");
	
	public static final CloudOKExceptionMessage VERIFY_CODE_WRONG =new CloudOKExceptionMessage("BUS.VERIFY_CODE_ERROR", "Verify code is wrong");
	
	public static final CloudOKExceptionMessage EMAIL_ALREADY_EXISTS =new CloudOKExceptionMessage("BUS.EMAIL_ALREADY_EXISTS", "Email is already register");
	
	public static final CloudOKExceptionMessage INVALID_EMAIL_ADDRESS =new CloudOKExceptionMessage("BUS.EMAIL_ALREADY_EXISTS", "invalid email address");

	public static final CloudOKExceptionMessage USERNAME_ALREADY_EXISTS =new CloudOKExceptionMessage("BUS.USERNAME_ALREADY_EXISTS", "User name is already register");

	public static final CloudOKExceptionMessage PHONE_ALREADY_EXISTS =new CloudOKExceptionMessage("BUS.PHONE_ALREADY_EXISTS", "Phone is already register");
	
	public static final CloudOKExceptionMessage EMAIL_NOT_FOUND =new CloudOKExceptionMessage("BUS.EMAIL_NOT_FOUND", "Email is not exists");
	
	public static final CloudOKExceptionMessage PHONE_NOT_FOUND =new CloudOKExceptionMessage("BUS.PHONE_NOT_FOUND", "Phone is not exists");

	public static final CloudOKExceptionMessage UNKNOW_VERIFY_CODE_TYPE =new CloudOKExceptionMessage("BUS.UNKNOW_VERIFY_CODE_TYPE", "Uknow verify code type");
	
	public static final CloudOKExceptionMessage TOO_MANY_VERIFY_CODE =new CloudOKExceptionMessage("BUS.TOO_MANY_VERIFY_CODE", "Too many verify code");
	
	public static final CloudOKExceptionMessage ORG_EXISTS =new CloudOKExceptionMessage("BUS.ORG_EXISTS", "The organizatioin is regiestered, please contact the administrator");
	
	public static final CloudOKExceptionMessage TOKEN_NOT_EXISTS =new CloudOKExceptionMessage("BUS.TOKEN_NOT_EXISTS", "This link is invalid or expired, Please contact the administrator.");

	public static final CloudOKExceptionMessage TOKEN_EXPIRED =new CloudOKExceptionMessage("BUS.TOKEN_EXPIRED", "This link is invalid or expired, Please contact the administrator.");
	
	public static final CloudOKExceptionMessage DEFAULT_ERROR =new CloudOKExceptionMessage("BUS.DEFAULT_ERROR", "System error.");
	

	public static CoreExceptionMessage build(String code, String message) {
		return new CoreExceptionMessage(code,message);
	}

	public static CoreExceptionMessage build(String message) {
		return new CoreExceptionMessage(UNDEFINED_ERROR,message);
	}
	
	public static CoreExceptionMessage build(Throwable exception) {
		return new CoreExceptionMessage(UNDEFINED_ERROR,exception.getMessage());
	}
}
