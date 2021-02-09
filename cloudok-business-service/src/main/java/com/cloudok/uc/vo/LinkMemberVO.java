package com.cloudok.uc.vo;

import lombok.Data;

@Data
public class LinkMemberVO extends MemberVO{

	private static final long serialVersionUID = 2694371440225222230L;

	/**
	 *  我是否关注了他 （当前登录用户是否关注了这个member）
	 */
	private boolean to;
	
	/**
	 * 他是否关注了我  （这个member是否关注了当前登录用户）
	 */
	private boolean from;
}
