package com.cloudok.authority.service.grant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrantObjectDynaSQL implements Serializable {

	private static final long serialVersionUID = 3426171063995160243L;

	private String dynaSQL;

	private Map<String, Object> params;

	public String getDynaSQL() {
		return dynaSQL;
	}

	public void setDynaSQL(String dynaSQL) {
		this.dynaSQL = dynaSQL;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public static GrantObjectDynaSQL buildObjectQuerySQL(GrantConfiguer configuer, String search, int pageNo,
			int pageSize,Long roleId) {
		if (pageNo <= 0) {
			pageNo = 1;
		}
		if (pageSize <= 0) {
			pageSize = 5;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		for (GrantConfiguerField field : configuer.getConfiguerFields()) {
			sql.append(field.getColumnName()).append(" as ").append(field.getFieldName()).append(",");
		}
		sql.append("t.id,t1.id as objectRoleId from ");
		sql.append(configuer.getTableName()).append(
				" left join sys_obj_role t1 on t1.obj_id=t.id and t1.obj_type=#{params.objType} and t1.deleted=false and t1.role_id=#{params.roleId}  where t.deleted=false and ( ");
		params.put("objType", configuer.getGrantObjectInfo().getCode());
		params.put("roleId", String.valueOf(roleId));
		boolean hasSearch = false;
		for (GrantConfiguerField field : configuer.getConfiguerFields()) {
			if (field.isSearchField()) {
				if (hasSearch == true) {
					sql.append(" OR ");
				}
				sql.append(field.getColumnName()).append(" like #{params.").append(field.getFieldName()).append("} ");
				params.put(field.getFieldName(), "%" + search + "%");
				hasSearch = true;
			}
		}
		if (!hasSearch) {
			sql.append(" 1=1 ");
		}
		sql.append(" ) ");
		sql.append(" limit ").append((pageNo - 1) * pageSize).append(",").append(pageSize);
		GrantObjectDynaSQL g = new GrantObjectDynaSQL();
		g.setDynaSQL(sql.toString());
		g.setParams(params);
		return g;
	}

	public static GrantObjectDynaSQL buildObjectQueryCountSQL(GrantConfiguer configuer, String search) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1)");
		sql.append("from ");
		sql.append(configuer.getTableName()).append(" where t.deleted=false and ( ");
		boolean hasSearch = false;
		for (GrantConfiguerField field : configuer.getConfiguerFields()) {
			if (field.isSearchField()) {
				if (hasSearch == true) {
					sql.append(" OR ");
				}
				sql.append(field.getColumnName()).append(" like #{params.").append(field.getFieldName()).append("} ");
				params.put(field.getFieldName(), "%" + search + "%");
				hasSearch = true;
			}
		}
		if (!hasSearch) {
			sql.append(" 1=1 ");
		}
		sql.append(" ) ");

		GrantObjectDynaSQL g = new GrantObjectDynaSQL();
		g.setDynaSQL(sql.toString());
		g.setParams(params);
		return g;
	}
	
	@SuppressWarnings("rawtypes")
	public static  GrantObjectDynaSQL buildUserRolesSQL(Map<String,RoleGrantHandler> handMaps,Long userId) {
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params=new HashMap<String, Object>();
		handMaps.forEach((k,v)->{
			if(sql.length()>0) {
				sql.append(" union ");
			}
			sql.append(" select t.id,t.role_code,t.role_name,t.remark,t.role_type,t.deleted,t.create_by,t.create_ts,t.update_by,t.update_ts from sys_role t ");
			sql.append(" left join sys_obj_role t1 on t.id=t1.role_id where t.deleted=false and t1.deleted=false ");
			sql.append(" and  t1.obj_type=#{params.roleType_").append(k).append("}");
			params.put("roleType_"+k, k);
			sql.append(" and t1.obj_id in (");
			
			@SuppressWarnings("unchecked")
			List<Long> ids=v.getObjectsByUser(userId);
			for(int i=0;i<ids.size();i++) {
				if(i<ids.size()-1) {
					sql.append(",");
				}
				sql.append("#{params.").append(k).append("_c_").append(i).append(",jdbcType=BIGINT}");
				params.put(k+"_c_"+i,ids.get(i));
			}
			sql.append(")");
		});
		GrantObjectDynaSQL grantObjectDynaSQL=new GrantObjectDynaSQL();
		grantObjectDynaSQL.setDynaSQL(sql.toString());
		grantObjectDynaSQL.setParams(params);
		return grantObjectDynaSQL;
	}
}
