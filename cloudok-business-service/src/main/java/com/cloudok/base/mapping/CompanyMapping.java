package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class CompanyMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping NAME=new Mapping("name", "t.name", QueryOperator.LIKE);
	
	public static final Mapping SN = new Mapping("sn", "t.sn",true);
}
