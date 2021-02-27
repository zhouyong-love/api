package com.cloudok.uc.vo;

import com.cloudok.uc.dto.SimpleMemberInfo;

import lombok.Data;

@Data
public class MessageThreadGroupItem {
	
	private SimpleMemberInfo simpleMemberInfo;
	
	private Integer unReadCount; //没回的总数
	
	private Integer totalCount; //所有的总数
	
	private Long lastetThreadId; //最后一个发送的threadId
	
}
