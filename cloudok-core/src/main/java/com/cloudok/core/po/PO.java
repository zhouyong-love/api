package com.cloudok.core.po;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:02:23
 * @param <PK>
 */

@Getter
@Setter
public abstract class PO implements Serializable{

	private static final long serialVersionUID = 3795348513693473147L;

	private Long id;
	
	private Long tenantId;

	private Boolean deleted;
	
	private Long createBy;

	private Long updateBy;

	private Timestamp createTs;

	private Timestamp updateTs;
}
