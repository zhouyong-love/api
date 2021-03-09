package com.cloudok.uc.vo;

import java.util.List;

import com.cloudok.uc.dto.SimpleMemberInfo;
import com.cloudok.uc.dto.WholeMemberDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuggestResult {
	// 已经推荐了几个
	private Integer suggested;
	// 已经推荐了几次
	private Integer times;
	// 成功推荐了几次  ---得到数据等于3个
	private Integer successTimes;
	// 失败推荐了几次  ---得到数据小于3个
	private Integer failedTimes;
	// 推荐列表
	private List<WholeMemberDTO> suggestList;
	// 今日关注列表
	private List<WholeMemberDTO> todayRecognizedList;
	// 今日推荐列表 包含已可，未可，以及当前推荐数据
	private List<SimpleMemberInfo> todaySuggestList;
}
