package com.cloudok.base.mapper;

import com.cloudok.base.vo.SchoolVO;
import com.cloudok.core.mapper.IMapper;
import com.cloudok.base.po.SchoolPO;

import java.util.List;

public interface SchoolMapper extends IMapper<SchoolPO>{

    List<SchoolVO> listByArea(Long areaId);
}
