package com.cloudok.authority.service;

import com.cloudok.authority.po.RoleResPO;
import com.cloudok.authority.vo.ResourceVO;
import com.cloudok.authority.vo.RoleResVO;
import com.cloudok.core.service.IService;

import java.util.List;

public interface RoleResService extends IService<RoleResVO,RoleResPO>{

	void deleteByRole(List<Long> roleIds);
	
	 List<ResourceVO> getResourceByRole(List<Long> roleIds);
}
