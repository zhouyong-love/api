package com.cloudok.uc.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SuggestedHistory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SuggestedHistoryItem> list;
}
