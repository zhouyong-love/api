package com.cloudok.authority.service.grant;

import java.io.Serializable;
import java.util.List;

public class GrantConfiguer implements Serializable {

	private static final long serialVersionUID = -809441708014214072L;

	private GrantObjectInfo grantObjectInfo;

	private String tableName;

	private List<GrantConfiguerField> configuerFields;

	public GrantObjectInfo getGrantObjectInfo() {
		return grantObjectInfo;
	}

	public void setGrantObjectInfo(GrantObjectInfo grantObjectInfo) {
		this.grantObjectInfo = grantObjectInfo;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<GrantConfiguerField> getConfiguerFields() {
		return configuerFields;
	}

	public void setConfiguerFields(List<GrantConfiguerField> configuerFields) {
		this.configuerFields = configuerFields;
	}
}
