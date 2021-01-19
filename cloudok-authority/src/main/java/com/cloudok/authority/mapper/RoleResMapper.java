package com.cloudok.authority.mapper;

import com.cloudok.authority.po.RoleResPO;
import com.cloudok.core.mapper.IMapper;

import java.util.List;

public interface RoleResMapper extends IMapper<RoleResPO>{

	 void deleteByRole(List<Long> roleIds);
}
