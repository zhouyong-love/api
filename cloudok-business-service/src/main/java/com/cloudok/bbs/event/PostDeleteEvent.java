package com.cloudok.bbs.event;

import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.event.BusinessEvent;

public class PostDeleteEvent extends BusinessEvent<PostVO>{

	private static final long serialVersionUID = 1L;
	
	public PostDeleteEvent(PostVO source) {
		super(source);
	}

	

}
