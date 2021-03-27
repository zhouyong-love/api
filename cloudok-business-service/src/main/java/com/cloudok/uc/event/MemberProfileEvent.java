package com.cloudok.uc.event;

import com.cloudok.core.event.BusinessEvent;
import com.cloudok.enums.ActionType;
import com.cloudok.enums.MemberProfileType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberProfileEvent extends BusinessEvent<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ActionType actionType;

	private Object newObj;

	private Object oldObj;

	private MemberProfileType type;
	
	private MemberProfileEvent(Long memberId) {
		super(memberId);
	}
	
	public static MemberProfileEvent create(Long memberId, MemberProfileType type, Object newObj) {
		return new MemberProfileEvent(memberId,ActionType.CREATE,type,newObj,null);
	}

	public static MemberProfileEvent update(Long memberId, MemberProfileType type, Object newObj,Object oldObj) {
		return new MemberProfileEvent(memberId,ActionType.UPDATE,type,newObj,oldObj);
	}

	public static MemberProfileEvent delete(Long memberId, MemberProfileType type, Object deleteObj) {
		return new MemberProfileEvent(memberId,ActionType.DELETE,type,deleteObj,null);
	}

	
	private MemberProfileEvent(Long memberId, ActionType actionType,MemberProfileType type, Object newObj, Object oldObj) {
		super(memberId);
		this.actionType = actionType;
		this.newObj = newObj;
		this.oldObj = oldObj;
		this.type = type;
	}
	 
}
