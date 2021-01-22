package com.cloudok.base.dict.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DictDataPO extends PO {

	private static final long serialVersionUID = 802192961124670300L;

	
	private String dictCode;
	
	
	private String dictShowName;
	
	
	private String dictValue;
	
	
	private String remark;
	
	
	private Long sn;
	
	
}
