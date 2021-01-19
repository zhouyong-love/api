package com.cloudok.base.vo;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DictDataVO extends VO {

	private static final long serialVersionUID = 702345616580168400L;
	
	
	private String dictCode;
	
	
	private String dictShowName;
	
	
	private String dictValue;
	
	
	private String remark;
	
	
	private Long sn=0L;
	
	private boolean builtInSystem=false;
	
	
}
