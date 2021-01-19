package com.cloudok.core.query;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 下午11:45:51
 */
public enum QueryOperator {

	/**
	 * 等于
	 */
	EQ,
	/**
	 * 大于
	 */
	GT,
	/**
	 * 小于
	 */
	LT,
	/**
	 * 大于等于
	 */
	GTE,
	/**
	 * 小于等于
	 */
	LTE,
	/**
	 * like %%
	 */
	LIKE,
	/**
	 * like %x
	 */
	LLIKE,
	/**
	 * like x%
	 */
	RLIKE,
	/**
	 * IN
	 */
	IN,
	/**
	 *  not null
	 */
	NE, 
	/**
	 * is null
	 */
	IE,
	/**
	 * !=
	 */
	NEQ
}
