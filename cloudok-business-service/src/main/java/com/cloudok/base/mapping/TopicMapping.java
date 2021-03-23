package com.cloudok.base.mapping;

import com.cloudok.core.mapping.Mapping;

public class TopicMapping extends Mapping{

	private static final long serialVersionUID = 0L;
	
	public static final Mapping TOPICID=new Mapping("topicId", "t.topic_id");
	
	public static final Mapping TOPICTYPE=new Mapping("topicType", "t.topic_type");
	
	public static final Mapping TOPICNAME=new Mapping("topicName", "t.topic_name");
	
	public static final Mapping TOPICICON=new Mapping("topicIcon", "t.topic_icon");
	
	public static final Mapping POSTCOUNT=new Mapping("postCount", "t.post_count");
	
	public static final Mapping PEERCOUNT=new Mapping("peerCount", "t.peer_count");
	
	public static final Mapping LASTUPDATETS=new Mapping("lastUpdateTs", "t.last_update_ts");
	
}
