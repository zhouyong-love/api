package com.cloudok.bbs.event;

import com.cloudok.bbs.vo.ThumbsUpVO;
import com.cloudok.core.event.BusinessEvent;

public class ThumbsUpCreateEvent extends BusinessEvent<ThumbsUpVO>{

	private static final long serialVersionUID = 1L;
	
	public ThumbsUpCreateEvent(ThumbsUpVO source) {
		super(source);
	}

	

}
