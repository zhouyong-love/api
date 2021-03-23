package com.cloudok.bbs.po;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThumbsUpPO extends PO {

	private static final long serialVersionUID = 759388623534810400L;

	private Integer businessType;

	private Long businessId;

//	@Deprecated
//	private Integer status;
//
//	@Deprecated
//	private Timestamp statusTs;
}
