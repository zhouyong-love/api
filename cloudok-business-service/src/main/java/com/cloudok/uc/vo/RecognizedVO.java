package com.cloudok.uc.vo;

import java.sql.Timestamp;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecognizedVO extends VO {

	private static final long serialVersionUID = 587804923605414400L;
	
	public RecognizedVO(){}
	
	public RecognizedVO(Long id,Boolean read){setId(id);this.read = read;}

	
	private Long sourceId;
	
	
	private Long targetId;
	
	private Boolean read;

	private Timestamp readTime;
	
}
