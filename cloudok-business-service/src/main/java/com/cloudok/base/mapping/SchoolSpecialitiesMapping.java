package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class SchoolSpecialitiesMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping SCHOOLID = new Mapping("schoolId", "t.school_id", QueryOperator.EQ);

	public static final Mapping SPECIALISMID = new Mapping("specialismId", "t.specialism_id", QueryOperator.EQ);
	
	

}
