package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;
import com.cloudok.uc.dto.SimpleMemberInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageVO extends VO {

	private static final long serialVersionUID = 106756808253635420L;
	
	
	private Integer type;
	
	private Long threadId;
	
	private SimpleMemberInfo from;
	
	
	private SimpleMemberInfo to;
	
	
	private String content;
	
	
	private Integer status;
	
	
	private java.sql.Timestamp statusTs;
	
	
	public MessageVO() {}
	
	public MessageVO(Long id,Integer status) {
		this.setId(id);
		this.status = status;
	}
	
}
