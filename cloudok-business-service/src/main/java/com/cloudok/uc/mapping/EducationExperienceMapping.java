package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class EducationExperienceMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping MEMBERID=new Mapping("memberId", "member_id");
	
	public static final Mapping SCHOOLID=new Mapping("schoolId", "school_id");
	
	public static final Mapping SPECIALISMID=new Mapping("specialismId", "specialism_id");
	
	public static final Mapping GRADE=new Mapping("grade", "grade");
	
	public static final Mapping DEGREE=new Mapping("degree", "degree");
	
}
