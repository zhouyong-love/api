package com.cloudok.core.query;

import java.io.Serializable;

import com.cloudok.core.mapping.Mapping;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月15日 下午9:48:11
 * @param <Q>
 */
public class SortCondition<Q extends QueryBuilder> implements Serializable {

	private static final long serialVersionUID = -2623890078759494033L;

	private Q q;
	private SortType sortType;

	private Mapping propertie;

	public SortCondition(Q q, Mapping propertie) {
		this.q = q;
		this.propertie = propertie;
	}

	protected SortCondition(Q q, Mapping propertie, SortType sortType) {
		this.q = q;
		this.propertie = propertie;
		this.sortType = sortType;
	}

	public SortType getSortType() {
		return sortType;
	}

	public Mapping getPropertie() {
		return propertie;
	}

	public String getColumn() {
		return propertie.getColumn();
	}

	/**
	 * 正序
	 * 
	 * @return
	 */
	public Q asc() {
		this.sortType = SortType.ASC;
		return q;
	}

	/**
	 * 倒序
	 * 
	 * @return
	 */
	public Q desc() {
		this.sortType = SortType.DESC;
		return q;
	}

}
