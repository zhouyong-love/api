package com.cloudok.authority.service.grant;

import java.util.List;

import com.cloudok.authority.vo.RoleVO;
import com.cloudok.core.po.PO;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 角色授予处理器
 * @author xiazhijian
 *
 */
public abstract class RoleGrantHandler<P extends PO> {

	/**
	 * 获取权限载体信息
	 * @return
	 */
	public abstract GrantObjectInfo objectTypeInfo();
	
	/**
	 * 映射
	 * @return
	 */
	public abstract GrantObjectMapping objectMapping();
	
	/**
	 * 获取权限载体类型
	 * @return
	 */
	public abstract Class<P> objectDataType();
	
	/**
	 * 获取用户与权限载体的关系
	 * @param user
	 * @return
	 */
	public abstract List<Long> getObjectsByUser(Long userId);
	
	@Autowired
	private RoleGrantAdapter roleGrantAdapter;
	
	/**
	 * 获取载体拥有的角色
	 * @param objectId
	 * @return
	 */
	public List<RoleVO> getObjectRoles(Long objectId){
		return roleGrantAdapter.getObjectRoles(objectTypeInfo().getCode(), objectId);
	}
}
