package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class ProjectExperienceMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping MEMBERID=new Mapping("memberId", "t.member_id",QueryOperator.EQ);
	
	public static final Mapping CATEGORY=new Mapping("category", "t.category",QueryOperator.EQ);
	
	public static final Mapping NAME=new Mapping("name", "t.name");
	
	public static final Mapping JOB=new Mapping("job", "t.job");
	
	public static final Mapping DESCRIPTION=new Mapping("description", "t.description");
	
	public static final Mapping SN = new Mapping("sn", "t.sn");
	
}
