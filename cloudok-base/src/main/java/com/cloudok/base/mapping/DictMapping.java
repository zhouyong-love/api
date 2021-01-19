package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;

public class DictMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping DICTNAME=new Mapping("dictName", "t.dict_name");
	
	public static final Mapping DICTCODE=new Mapping("dictCode", "t.dict_code");
	
	public static final Mapping REMARK=new Mapping("remark", "t.remark");
	
}
