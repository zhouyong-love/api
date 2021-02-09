package com.cloudok.uc.event;

import org.apache.commons.lang3.tuple.Pair;

import com.cloudok.core.event.BusinessEvent;

public class ViewMemberDetailEvent extends BusinessEvent<Pair<Long, Long>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViewMemberDetailEvent(Pair<Long, Long> source) {
		super(source);
	}

}
