package com.cloudok.bbs.event;

import com.cloudok.bbs.vo.CommentVO;
import com.cloudok.core.event.BusinessEvent;

public class CommentDeleteEvent extends BusinessEvent<CommentVO>{
 
	private static final long serialVersionUID = 1L;
 
	public CommentDeleteEvent(CommentVO source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

 
}
