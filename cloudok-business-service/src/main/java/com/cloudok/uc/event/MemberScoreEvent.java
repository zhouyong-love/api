package com.cloudok.uc.event;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.uc.vo.MemberVO;

public class MemberScoreEvent extends BusinessEvent<MemberVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MemberScoreEvent(MemberVO source) {
		super(source);
	}

}
