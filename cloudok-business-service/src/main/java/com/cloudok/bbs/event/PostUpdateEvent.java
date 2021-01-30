package com.cloudok.bbs.event;

import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.event.BusinessEvent;

public class PostUpdateEvent extends BusinessEvent<PostVO>{

	private static final long serialVersionUID = 1L;
	
	public PostUpdateEvent(PostVO source) {
		super(source);
	}

	

}
