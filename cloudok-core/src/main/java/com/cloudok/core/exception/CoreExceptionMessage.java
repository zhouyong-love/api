package com.cloudok.core.exception;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:01:01
 */

public class CoreExceptionMessage extends ExceptionMessage{

	public CoreExceptionMessage(String code, String message) {
		super(code, message);
	}

	private static final long serialVersionUID = 8239091920220949891L;
	
	public static final String UNDEFINED_ERROR="COMMON.UNDEFINED_ERROR";

	public static final CoreExceptionMessage CONVERT_ERROR=new CoreExceptionMessage("CORE.CONVERT_ERROR", "数据格式错误");
	
	public static final CoreExceptionMessage FIELD_NOTFOUND_ERR=new CoreExceptionMessage("CORE.FIELD_NOTFOUND_ERR", "数据格式错误");
	
	public static final CoreExceptionMessage SORT_ERR=new CoreExceptionMessage("CORE.SORT_ERR", "不支持排序字段");
	
	public static final CoreExceptionMessage QUERYOPEAR_ERR=new CoreExceptionMessage("CORE.QUERYOPEAR_ERR", "查询条件错误");
	
	public static final CoreExceptionMessage IDNULL_ERR = new CoreExceptionMessage("CORE.IDNULL_ERR", "id不能为空");
	
	public static final CoreExceptionMessage NOTFOUND_ERR = new CoreExceptionMessage("CORE.NOUNTFOUND_ERR", "请求资源未找到");
	
	public static final CoreExceptionMessage NO_PERMISSION = new CoreExceptionMessage("CORE.NO_PERMISSION", "没有权限");
	
	public static final CoreExceptionMessage GET_MULTIPLE = new CoreExceptionMessage("CORE.GET_MULTIPLE", "数据错误");
	
	public static final CoreExceptionMessage PARAMETER_ERR = new CoreExceptionMessage("CORE.PARAMETER_ERR", "请求参数错误");


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
