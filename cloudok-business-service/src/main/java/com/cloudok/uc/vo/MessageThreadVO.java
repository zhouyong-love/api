package com.cloudok.uc.vo;

import java.util.List;

import com.cloudok.core.vo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageThreadVO extends VO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2860265587390551270L;

	private Long threadId;

	private List<MessageVO> messageList;
	
	@Override
	public void setId(Long id) {
		super.setId(id);
		this.threadId = id;
	}
	

}
