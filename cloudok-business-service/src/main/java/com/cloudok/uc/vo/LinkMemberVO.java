package com.cloudok.uc.vo;

import lombok.Data;

@Data
public class LinkMemberVO extends MemberVO{

	private static final long serialVersionUID = 2694371440225222230L;

	private boolean to;
	
	private boolean from;
}
