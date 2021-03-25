package com.cloudok.bbs.vo;

import java.util.List;

import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.core.vo.VO;
import com.cloudok.uc.dto.SimpleMemberInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostVO extends VO {

	private static final long serialVersionUID = 993543937784132600L;

	private String content;

	private Integer thumbsUpCount;

	private Integer collectCount;

	private String attachIds;

	private Integer commentCount;

	//type目前支持  0 系统推荐 1 研究领域 2 行业 3 社团 4 个性 5 状态标签 6 学校 7 专业
	private Integer topicType;

	private Long topicId;
	
	private String topicName;
	
	private String topicIcon;

	private List<AttachVO> attachList;

	private List<CommentVO> commentList;

	private List<ThumbsUpVO> thumbsUpList;

	private SimpleMemberInfo memberInfo;
	
	private CommentVO latestComment;
	
	private Boolean myThumbsUp;

	/**
	 * post 修改事件用的数据，不返回其他任何地方
	 */
	@JsonIgnore
	private Integer oldTopicType;
	@JsonIgnore
	private Long oldTopicId;
	
	public PostVO() {
		
	}
	public PostVO(long id) {
		this.setId(id);
	}

}
