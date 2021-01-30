package com.cloudok.uc.event;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.uc.vo.RecognizedVO;

public class RecognizedDeleteEvent extends BusinessEvent<RecognizedVO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RecognizedDeleteEvent(RecognizedVO source) {
		super(source);
	}

}
