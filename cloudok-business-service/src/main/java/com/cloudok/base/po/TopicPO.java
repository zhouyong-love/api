package com.cloudok.base.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicPO extends PO {

	private static final long serialVersionUID = 449820296896617860L;

	
	private Long topicId;
	
	
	private Integer topicType;
	
	
	private String topicName;
	
	
	private String topicIcon;
	
	
	private Integer postCount;
	
	
	private Integer peerCount;
	
	
	private java.sql.Timestamp lastUpdateTs;
	
	private Long lastPostId;
	
}
