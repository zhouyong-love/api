package com.cloudok.base.service;

import com.cloudok.base.po.SchoolPO;
import com.cloudok.base.vo.SchoolVO;
import com.cloudok.core.service.IService;
import com.cloudok.core.vo.Page;

import java.util.List;

public interface SchoolService extends IService<SchoolVO, SchoolPO> {

    /**
     * 根据学校地区查询
     * @param areaId
     * @return
     */
    List<SchoolVO> listByArea(Long areaId);

    /**
     * 根据关键字查询学校
     * @param keywords
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<SchoolVO> searchSchool(String keywords, Integer pageNo, Integer pageSize);
}
