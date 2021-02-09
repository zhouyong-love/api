package com.cloudok.log.event;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.log.vo.SysLogVO;

public class UserActionEvent extends BusinessEvent<SysLogVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserActionEvent(SysLogVO source) {
		super(source);
	}

}
