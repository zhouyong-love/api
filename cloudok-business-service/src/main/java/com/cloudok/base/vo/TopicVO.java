package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicVO extends VO {

	private static final long serialVersionUID = 452235825583660300L;
	
	
	private Long topicId;
	
	
	private Integer topicType;
	
	
	private String topicName;
	
	
	private String topicIcon;
	
	
	private Integer postCount;
	
	
	private Integer peerCount;
	
	
	private java.sql.Timestamp lastUpdateTs;
	
	private Long lastPostId;
	
}
