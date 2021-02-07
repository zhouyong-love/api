package com.cloudok.uc.event;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.uc.vo.MessageVO;

public class MessageSendEvent extends BusinessEvent<MessageVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageSendEvent(MessageVO source) {
		super(source);
	}

}
