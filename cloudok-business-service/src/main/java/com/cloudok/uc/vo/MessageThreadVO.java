package com.cloudok.uc.vo;

import java.util.List;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageThreadVO extends VO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2860265587390551270L;

	private String threadId;

	private List<MessageVO> messageList;
	
	private Integer unReadCount;
	
	public MessageThreadVO(String threadId,List<MessageVO> messageList) {
		this.threadId = threadId;
		this.messageList = messageList;
	}
	
	
	

}
