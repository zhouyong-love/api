package com.cloudok.core.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:03:04
 * @param <PK>
 */

@Getter
@Setter
public abstract class VO implements Serializable{

	private static final long serialVersionUID = -3209098814612338063L;

	private Long id;
	
	private Long tenantId;
	
	private Boolean deleted;
	
	private Long createBy;
	
	private Long updateBy;
	
	private Timestamp createTs;

	private Timestamp updateTs;
}
