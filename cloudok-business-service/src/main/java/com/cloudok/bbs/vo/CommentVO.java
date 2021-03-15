package com.cloudok.bbs.vo;

import java.sql.Timestamp;

import com.cloudok.core.vo.VO;
import com.cloudok.uc.dto.SimpleMemberInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVO extends VO {

	private static final long serialVersionUID = 181319963814568160L;

	private String content;

	private Long postId;

	private String path;

	private Long parentId;

	private SimpleMemberInfo memberInfo;
	@Deprecated
	private Integer status;
	@Deprecated
	private Timestamp statusTs;

	private SimpleMemberInfo replyTo;

}
