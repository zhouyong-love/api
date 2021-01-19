package com.cloudok.cache;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CacheNameSpace implements Serializable{

	private static final long serialVersionUID = 2368942101276492371L;

	/**
	 * 缓存名称
	 */
	private String name;

	/**
	 * 缓存模块
	 */
	private String module;
	
	private String nameSpace;
	
	public CacheNameSpace(String module,String name) {
		this.name = name;
		this.module = module;
		this.nameSpace=module+"."+name;
	}
}
