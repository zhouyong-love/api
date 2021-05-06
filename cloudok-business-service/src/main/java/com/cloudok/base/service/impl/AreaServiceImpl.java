package com.cloudok.base.service.impl;

import com.cloudok.base.dto.AreaDTO;
import com.cloudok.base.mapper.AreaMapper;
import com.cloudok.base.mapping.AreaMapping;
import com.cloudok.base.po.AreaPO;
import com.cloudok.base.service.AreaService;
import com.cloudok.base.service.SchoolService;
import com.cloudok.base.vo.AreaVO;
import com.cloudok.base.vo.SchoolVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhouyong
 * @Date 2021/5/4 0004 1:44
 * @Version 1.0
 */
@Service
public class AreaServiceImpl extends AbstractService<AreaVO, AreaPO> implements AreaService {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    public AreaServiceImpl(AreaMapper repository) {
        super(repository);
    }

    @Override
    public List<AreaDTO> listAreaBySchool(HttpServletRequest request) {
        List<AreaVO> list = this.list(QueryBuilder.create(AreaMapping.class).with(request).sort(AreaMapping.SN).asc());
        List<AreaDTO> areaList = new ArrayList<>();
        if(null != list){
            for(AreaVO areaVO : list){
               AreaDTO areaDTO = new AreaDTO();
               List<SchoolVO> schoolList =  schoolService.listByArea(areaVO.getId());
               areaDTO.setId(areaVO.getId());
               areaDTO.setName(areaVO.getName());
               areaDTO.setSchoolList(schoolList);
               areaList.add(areaDTO);
            }
        }
        return areaList;
    }
}
