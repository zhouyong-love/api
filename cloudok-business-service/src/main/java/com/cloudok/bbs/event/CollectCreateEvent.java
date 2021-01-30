package com.cloudok.bbs.event;

import com.cloudok.bbs.vo.CollectVO;
import com.cloudok.core.event.BusinessEvent;

public class CollectCreateEvent extends BusinessEvent<CollectVO>{
 
	private static final long serialVersionUID = 1L;
 
	public CollectCreateEvent(CollectVO source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

 
}
