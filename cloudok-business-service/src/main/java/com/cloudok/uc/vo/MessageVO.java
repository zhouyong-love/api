package com.cloudok.uc.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageVO extends VO {

	private static final long serialVersionUID = 106756808253635420L;
	
	
	private Boolean type;
	
	
	private Long fromId;
	
	
	private Long toId;
	
	
	private String content;
	
	
	private Boolean status;
	
	
	private java.sql.Timestamp statusTs;
	
	
}
