package com.cloudok.log.mapping;

import com.cloudok.core.mapping.Mapping;

public class SysLogMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping MODELCODE=new Mapping("modelCode", "t.model_code");
	
	public static final Mapping MODELNAME=new Mapping("modelName", "t.model_name");
	
	public static final Mapping INTERFACECODE=new Mapping("interfaceCode", "t.interface_code");
	
	public static final Mapping INTERFACENAME=new Mapping("interfaceName", "t.interface_name");
	
	public static final Mapping USERID=new Mapping("userId", "t.user_id");
	
	public static final Mapping USERNAME=new Mapping("userName", "t.user_name");
	
	public static final Mapping REQUESTURL=new Mapping("requestUrl", "t.request_url");
	
	public static final Mapping REQUESTMETHOD=new Mapping("requestMethod", "t.request_method");
	
	public static final Mapping INPUT=new Mapping("input", "t.input");
	
	public static final Mapping INPUTLOGSWITCH=new Mapping("inputLogSwitch", "t.input_log_switch");
	
	public static final Mapping LOGLEVEL=new Mapping("logLevel", "t.log_level");
	
	public static final Mapping OUTPUTLOGSWITCH=new Mapping("outputLogSwitch", "t.output_log_switch");
	
	public static final Mapping OUTPUT=new Mapping("output", "t.output");
	
	public static final Mapping STATUS=new Mapping("status", "t.status");
	
	public static final Mapping FORMATEDMESSAGE=new Mapping("formatedMessage", "t.formated_message");
	
	public static final Mapping HTTPCODE=new Mapping("httpCode", "t.http_code");
	
	public static final Mapping BUSINESSID=new Mapping("businessId", "t.business_id");
	
	public static final Mapping RELATIONBUSINESSID=new Mapping("relationBusinessId", "t.relation_business_id");
	
}
