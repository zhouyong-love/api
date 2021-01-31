package com.cloudok.uc.po;

import lombok.Data;

@Data
public class LinkMemberPO extends MemberPO{

	private static final long serialVersionUID = 2694371440225222230L;

	private boolean to;
	
	private boolean from;
}
