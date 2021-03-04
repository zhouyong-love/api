package com.cloudok.bbs.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class PostMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping CONTENT = new Mapping("content", "t.content", QueryOperator.LIKE);

	public static final Mapping THUMBSUPCOUNT = new Mapping("thumbsUpCount", "t.thumbs_up_count");

	public static final Mapping REPLYCOUNT = new Mapping("replyCount", "t.reply_count");

	public static final Mapping COLLECTCOUNT = new Mapping("collectCount", "t.collect_count");
	
	public static final Mapping topicId = new Mapping("topicId", "t.topic_id");
	
	public static final Mapping topicType = new Mapping("topicType", "t.topic_type");
	

}
