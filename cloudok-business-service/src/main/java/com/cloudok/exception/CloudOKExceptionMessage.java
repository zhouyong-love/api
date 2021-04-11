package com.cloudok.exception;

import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.ExceptionMessage;

public class CloudOKExceptionMessage extends ExceptionMessage{

	public CloudOKExceptionMessage(String code, String message) {
		super(code, message);
	}
	
	public CloudOKExceptionMessage(int responseCode,String code, String message) {
		super(responseCode, code, message);
	}

	private static final long serialVersionUID = 8239091920220949891L;
	
	public static final String UNDEFINED_ERROR="COMMON.UNDEFINED_ERROR";

	public static final CloudOKExceptionMessage TEMPLATE_NOT_FOUND =new CloudOKExceptionMessage("BUS.TEMPLATE_NOT_FOUND", "验证码发送失败");
	
	public static final CloudOKExceptionMessage TEMPLATE_GENERATOR_ERROR =new CloudOKExceptionMessage("CORE.TEMPLATE_GENERATOR_ERROR", "验证码发送失败");
	
	public static final CloudOKExceptionMessage VERIFY_CODE_SEND_ERROR =new CloudOKExceptionMessage("BUS.VERIFY_CODE_SEND_ERROR", "验证码发送失败");
	
	public static final CloudOKExceptionMessage VERIFY_CODE_WRONG =new CloudOKExceptionMessage("BUS.VERIFY_CODE_ERROR", "验证码错误");
	
	public static final CloudOKExceptionMessage EMAIL_ALREADY_EXISTS =new CloudOKExceptionMessage("BUS.EMAIL_ALREADY_EXISTS", "邮箱已经被注册");
	
	public static final CloudOKExceptionMessage INVALID_EMAIL_ADDRESS =new CloudOKExceptionMessage("BUS.EMAIL_ALREADY_EXISTS", "邮箱地址错误");

	public static final CloudOKExceptionMessage USERNAME_ALREADY_EXISTS =new CloudOKExceptionMessage("BUS.USERNAME_ALREADY_EXISTS", "用户名已经注册");

	public static final CloudOKExceptionMessage PHONE_ALREADY_EXISTS =new CloudOKExceptionMessage("BUS.PHONE_ALREADY_EXISTS", "手机号已经被注册");
	
	public static final CloudOKExceptionMessage EMAIL_NOT_FOUND =new CloudOKExceptionMessage("BUS.EMAIL_NOT_FOUND", "邮箱不存");
	
	public static final CloudOKExceptionMessage PHONE_NOT_FOUND =new CloudOKExceptionMessage("BUS.PHONE_NOT_FOUND", "手机号不存在");

	public static final CloudOKExceptionMessage UNKNOW_VERIFY_CODE_TYPE =new CloudOKExceptionMessage("BUS.UNKNOW_VERIFY_CODE_TYPE", "未知验证码类型");
	
	public static final CloudOKExceptionMessage TOO_MANY_VERIFY_CODE =new CloudOKExceptionMessage("BUS.TOO_MANY_VERIFY_CODE", "验证码请求次数超过限制");
	
	public static final CloudOKExceptionMessage DEFAULT_ERROR =new CloudOKExceptionMessage("BUS.DEFAULT_ERROR", "系统错误");
	
	public static final CloudOKExceptionMessage REFRESH_TIMES_LIMIT =new CloudOKExceptionMessage("BUS.REFRESH_TIMES_LIMIT", "一天最多只能刷新5次");
	
	public static final CloudOKExceptionMessage SEARCH_KEYWORDS_IS_NULL =new CloudOKExceptionMessage("BUS.SEARCH_KEYWORDS_IS_NULL", "请输入搜索关键词");

	public static final CloudOKExceptionMessage INCOMPLETE_USER_INFORMATION =new CloudOKExceptionMessage(200,"BUS.INCOMPLETE_USER_INFORMATION", "用户信息不完整");

	public static final CloudOKExceptionMessage RECOGNIZED_SELF =new CloudOKExceptionMessage(200,"BUS.RECOGNIZED_SELF", "不能认可自己");

	public static final CloudOKExceptionMessage PARSE_WEIXIN_CODE_ERROR =new CloudOKExceptionMessage("BUS.PARSE_WEIXIN_CODE_ERROR", "解析微信code异常");
	
	public static final CloudOKExceptionMessage PARSE_WEIXIN_USER_INFO_ERROR =new CloudOKExceptionMessage("BUS.PARSE_WEIXIN_USER_INFO_ERROR", "解密用户信息失败");
	
	public static final CloudOKExceptionMessage PARSE_WEIXIN_PHONE_ERROR =new CloudOKExceptionMessage("BUS.PARSE_WEIXIN_PHONE_ERROR", "解密用户手机号失败");

	
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
