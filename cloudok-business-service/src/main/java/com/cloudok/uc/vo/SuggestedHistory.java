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
	// 已经推荐了几次
	private Integer times;
	// 成功推荐了几次 ---得到数据等于3个
	private Integer successTimes;
	// 失败推荐了几次 ---得到数据小于3个
	private Integer failedTimes;
	// 已经推荐的数据
	private List<SuggestedHistoryItem> list;
	// 上次推荐的数据
	private List<SuggestedHistoryItem> latestList;
	
	public SuggestedHistory() {
		
	}
	
	public SuggestedHistory(Integer times, Integer successTimes, Integer failedTimes, List<SuggestedHistoryItem> list) {
		super();
		this.times = times;
		this.successTimes = successTimes;
		this.failedTimes = failedTimes;
		this.list = list;
	}
	
	
	
}
