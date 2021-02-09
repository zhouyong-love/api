package com.cloudok.log.enums;

public enum SysLogLevel {

	/**
	 * 调试
	 */
	DEBUG(0),
	/**
	 * 缺省
	 */
	DEFAULT(1),
	/**
	 * 一般
	 */
	COMMONLY(2),
	/**
	 * 重要
	 */
	IMPORTANT(3),
	/**
	 * 关键
	 */
	CORE(4);

	SysLogLevel(int level) {
		this.level = level;
	}

	private int level;

	public int getLevel() {
		return level;
	}
 
	public static SysLogLevel  getByLevel(int level) {
		for(SysLogLevel  l : SysLogLevel.values()) {
			if(l.getLevel()==level) {
				return l;
			}
		}
		return null;
	}

}
