package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class TagMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping NAME = new Mapping("name", "t.name", QueryOperator.LIKE);

	public static final Mapping TYPE = new Mapping("type", "t.type", QueryOperator.EQ);

	public static final Mapping CATEGORY = new Mapping("category", "t.category", QueryOperator.EQ);

	public static final Mapping PARENTID = new Mapping("parentId", "t.parent_id", QueryOperator.EQ);

	public static final Mapping ICON = new Mapping("icon", "t.icon");

	public static final Mapping COLOR = new Mapping("color", "t.color");
	
	public static final Mapping SN = new Mapping("sn", "t.sn",true);

}
