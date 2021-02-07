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

	public static final CoreExceptionMessage CONVERT_ERROR=new CoreExceptionMessage("CORE.CONVERT_ERROR", "Data conversion exception.");
	
	public static final CoreExceptionMessage FIELD_NOTFOUND_ERR=new CoreExceptionMessage("CORE.FIELD_NOTFOUND_ERR", "Missing field.");
	
	public static final CoreExceptionMessage SORT_ERR=new CoreExceptionMessage("CORE.SORT_ERR", "Sorting is not supported.");
	
	public static final CoreExceptionMessage QUERYOPEAR_ERR=new CoreExceptionMessage("CORE.QUERYOPEAR_ERR", "Query criteria not supported.");
	
	public static final CoreExceptionMessage IDNULL_ERR = new CoreExceptionMessage("CORE.IDNULL_ERR", "ID is empty.");
	
	public static final CoreExceptionMessage NOTFOUND_ERR = new CoreExceptionMessage("CORE.NOUNTFOUND_ERR", "Not found");
	
	public static final CoreExceptionMessage NO_PERMISSION = new CoreExceptionMessage("CORE.NO_PERMISSION", "No permission");
	
	public static final CoreExceptionMessage GET_MULTIPLE = new CoreExceptionMessage("CORE.GET_MULTIPLE", "The get request returned multiple records");
	
	public static final CoreExceptionMessage PARAMETER_ERR = new CoreExceptionMessage("CORE.PARAMETER_ERR", "Parameter error");


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
