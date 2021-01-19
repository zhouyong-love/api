package com.cloudok.core.query;

import java.io.Serializable;

import com.cloudok.core.mapping.Mapping;

public class QueryProperty<V> implements Serializable{

	private static final long serialVersionUID = -3602272652937055815L;

	private QuerySymbol querySymbol;
	
	private Mapping property;
	
	private QueryOperator operator;
	
	private V value;
	
	public QuerySymbol getQuerySymbol() {
		return querySymbol;
	}

	public Mapping getProperty() {
		return property;
	}

	public QueryOperator getOperator() {
		return operator;
	}

	public V getValue() {
		return value;
	}

	public String getColumn() {
		return property.getColumn();
	}

	@SuppressWarnings("unchecked")
	public QueryProperty(QuerySymbol querySymbol,Mapping property,V value,QueryOperator operator) {
		this.querySymbol=querySymbol;
		this.property=property;
		if(value instanceof String) {
			if(value != null) {
				this.value = (V)String.class.cast(value).trim();
			}
		}else {
			this.value=value;
		}
		this.operator=operator;
	}
}
