package com.cloudok.bbs.event;

import com.cloudok.bbs.vo.CommentVO;
import com.cloudok.core.event.BusinessEvent;

public class CommentCreateEvent extends BusinessEvent<CommentVO>{
 
	private static final long serialVersionUID = 1L;
 
	public CommentCreateEvent(CommentVO source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

 
}
