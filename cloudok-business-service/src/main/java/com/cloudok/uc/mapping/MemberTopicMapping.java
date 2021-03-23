package com.cloudok.uc.mapping;

import com.cloudok.core.mapping.Mapping;

public class MemberTopicMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping MEMBERID=new Mapping("memberId", "t.member_id");
	
	public static final Mapping TOPICID=new Mapping("topicId", "t.topic_id");
	
	public static final Mapping LASTPOSTID=new Mapping("lastPostId", "t.last_post_id");
	
}
