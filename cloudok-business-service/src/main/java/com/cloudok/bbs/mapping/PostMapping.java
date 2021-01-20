package com.cloudok.bbs.mapping;

import com.cloudok.core.mapping.Mapping;

public class PostMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping CONTENT=new Mapping("content", "content");
	
	public static final Mapping THUMBSUPCOUNT=new Mapping("thumbsUpCount", "thumbs_up_count");
	
	public static final Mapping REPLYCOUNT=new Mapping("replyCount", "reply_count");
	
	public static final Mapping COLLECTCOUNT=new Mapping("collectCount", "collect_count");
	
}
