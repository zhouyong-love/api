package com.cloudok.uc.vo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
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
}
