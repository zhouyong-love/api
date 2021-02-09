package com.cloudok.log.enums;

public enum LogSwitch {

	/**
	 * 关
	 */
	OFF(0),
	/**
	 * 缺省
	 */
	DEFAULT(1),
	/**
	 * 开
	 */
	ON(2);

	LogSwitch(int swtich) {
		this.swtich = swtich;
	}

	private int swtich;

	public int getSwtich() {
		return swtich;
	}

}
