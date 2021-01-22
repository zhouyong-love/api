package com.cloudok.base.message.vo;

import java.util.ArrayList;
import java.util.List;

import com.cloudok.core.vo.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageVO extends VO {

	private static final long serialVersionUID = 255347468867930900L;
	
	
	private String messageType;
	
	
	private String messageContent;
	
	
	private String title;
	
	
	private Integer status;
	
	
	private String params;
	
	
	private String userName;

	private List<MessageDetailsVO> messageReceives;
	
	public void addReceive(MessageDetailsVO detailVO) {
		if(this.messageReceives==null) {
			this.messageReceives=new ArrayList<MessageDetailsVO>();
		}
		this.messageReceives.add(detailVO);
	}
	
}
