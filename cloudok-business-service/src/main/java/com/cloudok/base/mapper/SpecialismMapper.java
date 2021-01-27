package com.cloudok.base.mapper;

import java.util.List;

import com.cloudok.base.po.SpecialismPO;
import com.cloudok.core.mapper.IMapper;

public interface SpecialismMapper extends IMapper<SpecialismPO>{

	List<SpecialismPO> listBySchool(Long schoolId);

}
