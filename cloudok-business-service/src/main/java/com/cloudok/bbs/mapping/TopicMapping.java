package com.cloudok.bbs.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class TopicMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping NAME = new Mapping("name", "t.name", QueryOperator.LIKE);

	public static final Mapping ICON = new Mapping("icon", "t.icon");

	public static final Mapping STATUS = new Mapping("status", "t.status", QueryOperator.EQ);

	public static final Mapping POSTCOUNT = new Mapping("postCount", "t.post_count");

	public static final Mapping TYPE = new Mapping("type", "t.type", QueryOperator.EQ);

}
