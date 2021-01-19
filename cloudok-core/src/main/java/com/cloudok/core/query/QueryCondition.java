package com.cloudok.core.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cloudok.core.mapping.Mapping;


public class QueryCondition<Q extends QueryBuilder> implements Serializable {

	private static final long serialVersionUID = -2623890078759494033L;

	private Q q;
	
	private QuerySymbol querySymbol;
	
	public QuerySymbol getQuerySymbol() {
		return querySymbol;
	}

	public List<QueryProperty<?>> getProperties() {
		return properties;
	}
	
	public QueryCondition() {}
	
	protected QueryCondition(Q q,Mapping prop,Object value,QueryOperator operator,QuerySymbol s ) {
		this.q=q;
		this.querySymbol=s;
		properties.add(new QueryProperty<>(null,prop,value,operator));
	}

	private List<QueryProperty<?>> properties=new ArrayList<>();
	
	protected <V> void init(Q q,Mapping prop,V value,QueryOperator operator,QuerySymbol s) {
		this.q=q;
		this.querySymbol=s;
		properties.add(new QueryProperty<>(null,prop,value,operator));
	}
	
	/**
	 * 结束当前条件
	 * @return
	 */
	public Q end() {
		return q;
	}
	
	/**
	 * 添加查询列 and
	 * @param prop
	 * @param value
	 * @param operator
	 * @return
	 */
	public <V> QueryCondition<Q> and(Mapping prop,QueryOperator operator,V value) {
		properties.add(new QueryProperty<>(QuerySymbol.AND,prop,value,operator));
		return this;
	}
	/**
	 * 添加查询列 or
	 * @param prop
	 * @param value
	 * @param operator
	 * @return
	 */
	public <V> QueryCondition<Q> or(Mapping prop,QueryOperator operator,V value) {
		properties.add(new QueryProperty<>(QuerySymbol.OR,prop,value,operator));
		return this;
	}
	
	/**
	 * 添加查询列 and
	 * @param prop
	 * @param value
	 * @return
	 */
	public <V> QueryCondition<Q> and(Mapping prop,V value) {
		return and(prop,QueryOperator.EQ, value);
	}
	
	/**
	 * 添加查询列 or
	 * @param prop
	 * @param value
	 * @return
	 */
	public <V> QueryCondition<Q> or(Mapping prop,V value) {
		return or(prop, QueryOperator.EQ, value);
	}
}
