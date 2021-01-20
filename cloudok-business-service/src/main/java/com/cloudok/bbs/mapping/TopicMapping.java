package com.cloudok.bbs.mapping;

import com.cloudok.core.mapping.Mapping;

public class TopicMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping NAME=new Mapping("name", "name");
	
	public static final Mapping ICON=new Mapping("icon", "icon");
	
	public static final Mapping STATUS=new Mapping("status", "status");
	
	public static final Mapping POSTCOUNT=new Mapping("postCount", "post_count");
	
	public static final Mapping TYPE=new Mapping("type", "type");
	
}
