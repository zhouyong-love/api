package com.cloudok.uc.vo;

import java.util.List;

import lombok.Data;

@Data
public class MessageThreadGroup {

	private Integer totalCount;
	
	private Integer unReadTotalCount;
	
	private List<MessageThreadGroupItem> list;
	
	private Integer memberTotalCount;
}
