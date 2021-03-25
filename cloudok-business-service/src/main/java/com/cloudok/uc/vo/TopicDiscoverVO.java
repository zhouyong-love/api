package com.cloudok.uc.vo;

import java.util.List;

import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.vo.VO;
import com.cloudok.uc.dto.SimpleMemberInfo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopicDiscoverVO extends VO {
	private static final long serialVersionUID = 4674639161901483114L;
	// type目前支持 0 系统推荐 1 研究领域 2 行业 3 社团 4 个性 5 状态标签 6 学校 7 专业

	private Long topicId;

	private Integer topicType;

	private String topicName;

	private String topicIcon;

	private Integer postCount;

	private Integer peerCount;
	
	private Integer updatePostCount;

	private java.sql.Timestamp lastUpdateTs;

	private PostVO latestPost;

	private List<SimpleMemberInfo> peersList;
}
