package com.cloudok.base.message.exception;

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


	public static final MessageExceptionMessges TEMPLATE_NOT_FOUND =new MessageExceptionMessges("MES.TEMPLATE_NOT_FOUND", "模板未找到");
	
	public static final MessageExceptionMessges MULTIPLE_TEMPLATE_FOUND =new MessageExceptionMessges("MES.MULTIPLE_TEMPLATE_FOUND", "多个相同模板冲突");
	
	public static final MessageExceptionMessges TEMPLATE_GENERATOR_ERROR =new MessageExceptionMessges("MES.TEMPLATE_GENERATOR_ERROR", "模板生成失败");
	
	public static final MessageExceptionMessges READ_TEMPLATE_FAILED =new MessageExceptionMessges("MES.READ_TEMPLATE_FAILED", "读取模板文件失败");
	
	public static final MessageExceptionMessges RECEIVER_NOT_EXISTS =new MessageExceptionMessges("MES.RECEIVER_NOT_EXISTS", "接收对象不存在"); 
	

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