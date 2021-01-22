package com.cloudok.base.dict.mapping;

import com.cloudok.core.mapping.Mapping;

public class DictDataMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping DICTCODE=new Mapping("dictCode", "t.dict_code");
	
	public static final Mapping DICTSHOWNAME=new Mapping("dictShowName", "t.dict_show_name");
	
	public static final Mapping DICTVALUE=new Mapping("dictValue", "t.dict_value");
	
	public static final Mapping REMARK=new Mapping("remark", "t.remark");
	
	public static final Mapping SN=new Mapping("sn", "t.sn");
	
}
