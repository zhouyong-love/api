package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class EducationExperienceMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping MEMBERID = new Mapping("memberId", "t.member_id", QueryOperator.EQ);

	public static final Mapping SCHOOLID = new Mapping("schoolId", "t.school_id", QueryOperator.EQ);

	public static final Mapping SPECIALISMID = new Mapping("specialismId", "t.specialism_id", QueryOperator.EQ);

	public static final Mapping GRADE = new Mapping("grade", "t.grade", QueryOperator.EQ);

	public static final Mapping DEGREE = new Mapping("degree", "t.degree", QueryOperator.EQ);

	public static final Mapping SN = new Mapping("sn", "t.sn");
}
