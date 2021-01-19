package com.cloudok.core.mapping;

import java.io.Serializable;

import com.cloudok.core.query.QueryOperator;
/**
 * 查询映射配置
 */
public class Mapping implements Serializable{
	
	public static final Mapping ID=new Mapping("id", "t.id",true);

	public static final Mapping DELETED=new Mapping("deleted", "t.deleted");
	
	public static final Mapping CREATEBY=new Mapping("createBy", "t.create_by");
	
	public static final Mapping CREATETIME=new Mapping("createTs", "t.create_ts",true);
	
	public static final Mapping MODIFYBY=new Mapping("updateBy", "t.update_by");
	
	public static final Mapping MODIFYTIME=new Mapping("updateTs", "t.update_ts",true);
	
	private static final long serialVersionUID = 1197717290155508458L;

	private String field;
	
	private String column;
	
	private QueryOperator[] operator;
	
	private Boolean sort=false;
	
	public Mapping() {}
	
	public Mapping(String field, String column,Boolean sort, QueryOperator... operator) {
		super();
		this.field = field;
		this.column = column;
		this.sort=sort;
		this.operator = operator;
		
	}
	
	public Mapping(String field, String column, QueryOperator... operator) {
		super();
		this.field = field;
		this.column = column;
		this.operator = operator;
		
	}
	
	public Mapping(String field, String column,Boolean sort) {
		super();
		this.field = field;
		this.column = column;
		this.sort=sort;
	}
	
	public Mapping(String field, String column) {
		super();
		this.field = field;
		this.column = column;
	}

	public Boolean getSort() {
		return sort;
	}

	public void setSort(Boolean sort) {
		this.sort = sort;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public QueryOperator[] getOperator() {
		return operator;
	}

	public void setOperator(QueryOperator[] operator) {
		this.operator = operator;
	}
}
