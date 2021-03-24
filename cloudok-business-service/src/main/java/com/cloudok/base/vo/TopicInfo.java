package com.cloudok.base.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TopicInfo {
	private Long topicId;

	private Integer topicType;

	private String topicName;

	private String topicIcon;
	
	private Boolean forceUpate;

}
