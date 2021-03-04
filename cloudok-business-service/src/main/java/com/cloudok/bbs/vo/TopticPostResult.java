package com.cloudok.bbs.vo;

import com.cloudok.core.vo.Page;

import lombok.Data;

@Data
public class TopticPostResult {
	private Page<PostVO> postPage;
	private Long postCount;
	private Long peersCount;
}
