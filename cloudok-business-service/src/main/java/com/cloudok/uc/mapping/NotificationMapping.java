package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class NotificationMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping BUSINESSTYPE=new Mapping("businessType", "t.business_type");
	
	public static final Mapping BUSINESSID=new Mapping("businessId", "t.business_id");
	
	public static final Mapping TITLE=new Mapping("title", "t.title");
	
	public static final Mapping REMARK=new Mapping("remark", "t.remark");
	
	public static final Mapping STATUS=new Mapping("status", "t.status");
	
	public static final Mapping STATUSTS=new Mapping("statusTs", "t.status_ts");
	
	public static final Mapping MEMBERID=new Mapping("memberId", "t.member_id");
	
	
	
}
