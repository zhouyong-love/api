package com.cloudok.uc.service.impl;

import org.springframework.context.ApplicationListener;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.uc.event.MemberUpdateEvent;
import com.cloudok.uc.event.MessageSendEvent;
import com.cloudok.uc.event.RecognizedCreateEvent;
import com.cloudok.uc.event.ViewMemberDetailEvent;

public class MemberScoreCalcService implements ApplicationListener<BusinessEvent<?>> {

	@Override
	public void onApplicationEvent(BusinessEvent<?> arg0) {
		
		if (arg0 instanceof RecognizedCreateEvent) {

		}
		
		if (arg0 instanceof MemberUpdateEvent) {

		}
		
		if (arg0 instanceof MessageSendEvent) {

		}
		
		if (arg0 instanceof ViewMemberDetailEvent) {

		}
		
		

	}

}
