package com.cloudok.authority.service;

import com.cloudok.authority.po.ResourcePO;
import com.cloudok.authority.vo.ResourceVO;
import com.cloudok.core.service.IService;

import java.util.List;

public interface ResourceService extends IService<ResourceVO,ResourcePO>{

	List<ResourceVO> getResourcesByUser(Long userid);
	
	List<ResourceVO> getMenu(Long userid);
	
	List<ResourceVO> queryRoleRes(List<Long> roleIds);
}
