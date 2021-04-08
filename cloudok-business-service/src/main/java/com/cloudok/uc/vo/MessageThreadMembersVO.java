package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageThreadMembersVO extends VO {

	private static final long serialVersionUID = 514910815917045250L;
	
	
	private Long threadId;
	
	private Long memberId;
	
	private Long lastPosition;
	
	private Integer unRead;
	
	
}
