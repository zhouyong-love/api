package com.cloudok.uc.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月18日 下午12:11:41
 */
@Getter @Setter
public class SingupVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8296659047962555994L;

	private String registerType;
	
	private String email;
	
	private String phone;
	
	private String code;
	
	
}
