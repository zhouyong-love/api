package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class InternshipExperienceMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping MEMBERID=new Mapping("memberId", "member_id");
	
	public static final Mapping COMPANYID=new Mapping("companyId", "company_id");
	
	public static final Mapping JOBID=new Mapping("jobId", "job_id");
	
	public static final Mapping INDUSTRYID=new Mapping("industryId", "industry_id");
	
	public static final Mapping DESCRIPTION=new Mapping("description", "description");
	
}
