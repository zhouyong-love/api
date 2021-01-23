package com.cloudok.bbs.mapping;

import com.cloudok.core.mapping.Mapping;

public class PostTopicMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping TOPICID=new Mapping("topicId", "t.topic_id");
	
	public static final Mapping POSTID=new Mapping("postId", "t.post_id");
	
}
