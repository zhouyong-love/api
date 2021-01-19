package com.cloudok.authority.mapper;

import com.cloudok.authority.po.ResourcePO;
import com.cloudok.core.mapper.IMapper;

import java.util.List;

public interface ResourceMapper extends IMapper<ResourcePO>{

	List<ResourcePO> queryRoleRes(List<Long> roleIds);
}
