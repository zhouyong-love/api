package com.cloudok.bbs.mapping;

import com.cloudok.core.mapping.Mapping;

public class CommentMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping CONTENT=new Mapping("content", "content");
	
	public static final Mapping THUMBSUPCOUNT=new Mapping("thumbsUpCount", "thumbs_up_count");
	
	public static final Mapping REPLYCOUNT=new Mapping("replyCount", "reply_count");
	
	public static final Mapping COLLECTCOUNT=new Mapping("collectCount", "collect_count");
	
	public static final Mapping POSTID=new Mapping("postId", "post_id");
	
	public static final Mapping PATH=new Mapping("path", "path");
	
	public static final Mapping PARENTID=new Mapping("parentId", "parent_id");
	
}
