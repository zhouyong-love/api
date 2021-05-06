package com.cloudok.base.service;

import com.cloudok.base.dto.AreaDTO;
import com.cloudok.base.po.AreaPO;
import com.cloudok.base.vo.AreaVO;
import com.cloudok.core.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author zhouyong
 * @Date 2021/5/4 0004 1:33
 * @Version 1.0
 */
public interface AreaService extends IService<AreaVO, AreaPO> {

    List<AreaDTO> listAreaBySchool(HttpServletRequest request);
}
