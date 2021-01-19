package com.cloudok.authority.service.grant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Component;

import com.cloudok.authority.service.ObjRoleService;
import com.cloudok.authority.service.RoleService;
import com.cloudok.authority.vo.RoleVO;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.util.ReflectionUtils;

/**
 * 角色载体授权处理类
 * @author xiazhijian
 *
 */
@Component
@AutoConfigureAfter(value = RoleGrantHandler.class)
public class RoleGrantAdapter implements ApplicationRunner{
	
	/**
	 * 获取配置信息
	 * @return
	 */
	public Map<String, GrantConfiguer> getConfiguer(){
		return grantConfigures;
	}
	
	@SuppressWarnings("rawtypes")
	private Map<String,RoleGrantHandler> handlerMap;
	
	private Map<String, GrantConfiguer> grantConfigures;
	
	@Autowired
	private RoleService roleServer;
	
	@Autowired
	private ObjRoleService objectRoleServer;
	
	/**
	 * 获取载体所拥有的角色列表
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	public List<RoleVO> getObjectRoles(String objectType,Long objectId){
		return roleServer.findRolesByObject( objectType, objectId);
	}
	
	/**
	 * 获取用户拥有的角色
	 * @param userId
	 * @return
	 */
	public List<RoleVO> getUserRoles(Long userId){
		return roleServer.findUserRoles(GrantObjectDynaSQL.buildUserRolesSQL(this.handlerMap, userId));
	}
	
	public GrantObjectPage getObjects(String objectType,String search,int pageNo,int pageSize,Long roleId) {
		GrantObjectPage page=new GrantObjectPage();
		page.setTotalCount(objectRoleServer.queryObjectCount(GrantObjectDynaSQL.buildObjectQueryCountSQL(grantConfigures.get(objectType), search)));
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		if(page.getTotalCount()>0&&(page.getTotalCount()/pageSize+1)>=pageNo) {
			page.setData(objectRoleServer.queryObjectPage(GrantObjectDynaSQL.buildObjectQuerySQL(grantConfigures.get(objectType), search, pageNo, pageSize,roleId)));
		}
		return page;
	}

	@SuppressWarnings("rawtypes")
	private void init() {
		if(handlerMap==null) {
			handlerMap=new HashMap<String, RoleGrantHandler>();
			grantConfigures=new HashMap<String, GrantConfiguer>();
			Map<String, RoleGrantHandler> grants = SpringApplicationContext.getApplicationContext().getBeansOfType(RoleGrantHandler.class);
			for (RoleGrantHandler objectInfo : grants.values()) {
				if(handlerMap.containsKey(objectInfo.objectTypeInfo().getCode())) {
					throw new SystemException("Repeated definition of role carrier.");
				}
				handlerMap.put(objectInfo.objectTypeInfo().getCode(), objectInfo);
				initConfigure(objectInfo);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private void initConfigure(RoleGrantHandler grantHandler) {
		Class<?> classzz=grantHandler.objectDataType();
		GrantConfiguer configuer=new GrantConfiguer();
		configuer.setGrantObjectInfo(grantHandler.objectTypeInfo());
		configuer.setTableName(grantHandler.objectMapping().getTableName()+ " t");
		List<GrantConfiguerField> configuerFields=new ArrayList<GrantConfiguerField>();
		Map<String,GrantObjectField> grantAnnotations = getGrantObjectFieldAnnotation(grantHandler.objectDataType());
		if(grantAnnotations==null||grantAnnotations.isEmpty()) {
			throw  new SystemException(grantHandler.objectDataType().getName() +"class Missing  GrantObjectField annotation");
		}
		for (String fieldName : grantAnnotations.keySet()) {
			GrantObjectField field=grantAnnotations.get(fieldName);
			if(field==null) {
				continue;
			}
			GrantConfiguerField configuerField=new GrantConfiguerField();
			configuerField.setFieldLabel(field.fieldLabel());
			configuerField.setSearchField(field.searchField());
			configuerField.setOrder(field.order());
			configuerField.setFieldName(fieldName);
			Mapping fieldInfo = QueryBuilder.getMapping(ReflectionUtils.getAllFields(grantHandler.objectMapping().getMapping()), fieldName);
			if(fieldInfo==null) {
				throw  new SystemException(grantHandler.objectMapping().getMapping().getName() +"class Missing "+fieldName+" Mapping configure");
			}
			configuerField.setColumnName("t."+fieldInfo.getColumn());
			configuerFields.add(configuerField);
		}
		configuerFields.sort(new ConfiguerFieldSort());
		configuer.setConfiguerFields(configuerFields);
		grantConfigures.put(grantHandler.objectTypeInfo().getCode(), configuer);
	}
	
	private <C> Map<String,GrantObjectField> getGrantObjectFieldAnnotation(Class<C> c) {
		Map<String,GrantObjectField> map=new HashMap<String, GrantObjectField>();
		Class<?> clazz=c;
		while(clazz!=null){
			Field[] fieldArray = clazz.getDeclaredFields();
			for (Field field : fieldArray) {
				if(!map.containsKey(field.getName())){
					map.put(field.getName(), field.getAnnotation(GrantObjectField.class));
				}
			}
			clazz=clazz.getSuperclass();
		}
		return map;
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		init();
	}
}

class ConfiguerFieldSort implements Comparator<GrantConfiguerField>{

	@Override
	public int compare(GrantConfiguerField o1, GrantConfiguerField o2) {
		if(o1.getOrder()>o2.getOrder()) {
			return 1;
		}
		if(o1.getOrder()<o2.getOrder()) {
			return -1;
		}
		return 0;
	}
}