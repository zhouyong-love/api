package com.cloudok.uc.vo;

import java.util.List;

import com.cloudok.uc.dto.WholeMemberDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuggestResult {
	//已经刷新了几次
	private Integer times; 
	//推荐列表
	private List<WholeMemberDTO> suggestList;
	//今日关注列表
	private List<WholeMemberDTO> todayRecognizedList;
}
