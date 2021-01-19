package com.cloudok.core.vo;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import com.cloudok.core.exception.CoreExceptionMessage;
import com.cloudok.core.exception.ExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.json.JSON;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月15日 下午9:34:13
 */
@Getter @Setter
public class Response implements Serializable{

	private static final long serialVersionUID = -6904369030702853357L;

	private boolean status;
	
	private ErrorMessage error;

	private Object result;
	
	public static Response buildSuccess() {
		Response response = new Response();
		response.setStatus(true);
		return response;
	}
	
	public static <D> Response buildSuccess(D result) {
		Response response = new Response();
		response.setStatus(true);
		response.setResult(result);
		return response;
	}
	
	public static Response buildFail(SystemException ex) {
		Response response = new Response();
		response.setStatus(false);
		ErrorMessage error = new ErrorMessage();
		error.setErrorCode(ex.getExceptionMessage().getCode());
		error.setErrorMessage(StringUtils.isEmpty(ex.getMessage()) ? ex.getExceptionMessage().getMessage()
				: ex.getMessage());
		response.setError(error);
		return response;
	}
	
	public static Response buildFail(Exception ex) {
		Response response = new Response();
		response.setStatus(false);
		ErrorMessage error = new ErrorMessage();
		error.setErrorCode(CoreExceptionMessage.UNDEFINED_ERROR);
		error.setErrorMessage(ex.getMessage());
		response.setError(error);
		return response;
	}
	
	public static Response buildFail(ExceptionMessage message) {
		Response response = new Response();
		response.setStatus(false);
		ErrorMessage error = new ErrorMessage();
		error.setErrorCode(message.getCode());
		error.setErrorMessage(message.getMessage());
		response.setError(error);
		return response;
	}
	
	public static Response buildFail(String message) {
		Response response = new Response();
		response.setStatus(false);
		ErrorMessage error = new ErrorMessage();
		error.setErrorCode(CoreExceptionMessage.UNDEFINED_ERROR);
		error.setErrorMessage(message);
		response.setError(error);
		return response;
	}
	
	public String toString() {
		return JSON.toJSONString(this);
	}
}
