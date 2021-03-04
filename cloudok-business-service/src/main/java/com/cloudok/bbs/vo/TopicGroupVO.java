package com.cloudok.bbs.vo;

import java.util.List;

import com.cloudok.base.dict.enums.EnumInfo;

import lombok.Data;

@Data
public class TopicGroupVO {
	// type目前支持 0 系统推荐 1 研究领域 2 行业 3 社团 4 个性 5 状态标签 6 学校 7 专业
	private String type;
	private String name;
	private List<TopicVO> topicList;

	public TopicGroupVO() {
		
	}
	public TopicGroupVO(String type, String name) {
		super();
		this.type = type;
		this.name = name;
	}
	public TopicGroupVO(EnumInfo school) {
		this.type = school.getValue();
		this.name = school.getLabel();
	}

}
