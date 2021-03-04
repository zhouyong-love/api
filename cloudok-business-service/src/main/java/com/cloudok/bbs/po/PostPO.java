package com.cloudok.bbs.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPO extends PO {

	private static final long serialVersionUID = 322080766612610200L;

	private String content;

	private Integer thumbsUpCount;

	private Integer collectCount;

	private String attachIds;

	private Integer commentCount;

	// type目前支持 1 研究领域 2 行业 3 社团 4 个性 5 状态标签 6 学校 7 专业
	private Integer topicType;

	private Long topicId;

	private String topicName;
	
	private String topicIcon;

}
