package com.cloudok.bbs.event;

import com.cloudok.bbs.vo.ThumbsUpVO;
import com.cloudok.core.event.BusinessEvent;

public class ThumbsUpDeleteEvent extends BusinessEvent<ThumbsUpVO>{

	private static final long serialVersionUID = 1L;
	
	public ThumbsUpDeleteEvent(ThumbsUpVO source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	

}
