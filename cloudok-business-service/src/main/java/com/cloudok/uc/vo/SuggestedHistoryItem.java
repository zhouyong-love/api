package com.cloudok.uc.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class SuggestedHistoryItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Long targetId;
	private Integer status; //0未看过 1 已经看过
	
	public SuggestedHistoryItem() {
		
	}
	public SuggestedHistoryItem(Long targetId) {
		this.targetId = targetId;
	}
	public SuggestedHistoryItem(Long targetId,Integer status) {
		this.targetId = targetId;
		this.status = status;
	}
}
