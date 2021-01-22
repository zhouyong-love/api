package com.cloudok.base.dict.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DictVO extends VO {

	private static final long serialVersionUID = 598588167138643100L;
	
	
	private String dictName;
	
	
	private String dictCode;
	
	
	private String remark;
	
	private boolean builtInSystem=false;
}
