package com.cloudok.base.event;

import com.cloudok.base.vo.TopicInfo;
import com.cloudok.core.event.BusinessEvent;

public class TopicCreateEvent extends BusinessEvent<TopicInfo>{
 
	private static final long serialVersionUID = 1L;
 
	public TopicCreateEvent(TopicInfo source) {
		super(source);
	}

 
}
