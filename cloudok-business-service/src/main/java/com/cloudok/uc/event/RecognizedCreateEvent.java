package com.cloudok.uc.event;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.uc.vo.RecognizedVO;

public class RecognizedCreateEvent extends BusinessEvent<RecognizedVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RecognizedCreateEvent(RecognizedVO source) {
		super(source);
	}

}
