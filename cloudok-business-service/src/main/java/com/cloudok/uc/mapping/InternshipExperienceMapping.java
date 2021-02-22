package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class InternshipExperienceMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping MEMBERID = new Mapping("memberId", "t.member_id", QueryOperator.EQ);

	public static final Mapping COMPANYID = new Mapping("companyId", "t.company_id", QueryOperator.EQ);

	public static final Mapping JOBID = new Mapping("jobId", "t.job_id", QueryOperator.EQ);

	public static final Mapping INDUSTRYID = new Mapping("industryId", "t.industry_id", QueryOperator.EQ);

	public static final Mapping DESCRIPTION = new Mapping("description", "t.description");
	
	public static final Mapping SN = new Mapping("sn", "t.sn");

}
