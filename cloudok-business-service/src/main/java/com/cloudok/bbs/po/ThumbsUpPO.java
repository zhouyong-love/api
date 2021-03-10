package com.cloudok.bbs.po;

import java.sql.Timestamp;

import com.cloudok.core.po.PO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThumbsUpPO extends PO {

	private static final long serialVersionUID = 759388623534810400L;

	private Integer businessType;

	private Long businessId;

	private Integer status;

	private Timestamp statusTs;
}
