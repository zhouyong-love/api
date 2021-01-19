package com.cloudok.authority.service.grant;

import java.io.Serializable;

import com.cloudok.core.mapping.Mapping;

public class GrantObjectMapping implements Serializable{

	private static final long serialVersionUID = 431129478199621073L;
	
	public GrantObjectMapping(String tableName, Class<? extends Mapping> mapping) {
		super();
		this.tableName = tableName;
		this.mapping = mapping;
	}

	private String tableName;
	
	private Class<? extends Mapping> mapping;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Class<? extends Mapping> getMapping() {
		return mapping;
	}

	public void setMapping(Class<? extends Mapping> mapping) {
		this.mapping = mapping;
	}

}
