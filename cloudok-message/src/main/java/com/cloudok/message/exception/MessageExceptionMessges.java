package com.cloudok.message.exception;

import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.ExceptionMessage;

public class MessageExceptionMessges extends ExceptionMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2777859561803188705L;


	public MessageExceptionMessges(String code, String message) {
		super(code, message);
	}

	
	public static final String UNDEFINED_ERROR="MES.UNDEFINED_ERROR";


	public static final MessageExceptionMessges TEMPLATE_NOT_FOUND =new MessageExceptionMessges("MES.TEMPLATE_NOT_FOUND", "Template not found.");
	
	public static final MessageExceptionMessges MULTIPLE_TEMPLATE_FOUND =new MessageExceptionMessges("MES.MULTIPLE_TEMPLATE_FOUND", "Multiple template found.");
	
	public static final MessageExceptionMessges TEMPLATE_GENERATOR_ERROR =new MessageExceptionMessges("MES.TEMPLATE_GENERATOR_ERROR", "Generator failed.");
	
	public static final MessageExceptionMessges READ_TEMPLATE_FAILED =new MessageExceptionMessges("MES.READ_TEMPLATE_FAILED", "Read template file failed");
	
	public static final MessageExceptionMessges RECEIVER_NOT_EXISTS =new MessageExceptionMessges("MES.RECEIVER_NOT_EXISTS", "Receiver not exists."); 
	

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