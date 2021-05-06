package com.cloudok.base.service.impl;

import com.cloudok.base.mapping.SchoolMapping;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.core.vo.Page;
import com.cloudok.exception.CloudOKExceptionMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudok.base.mapper.SchoolMapper;
import com.cloudok.base.po.SchoolPO;
import com.cloudok.base.service.SchoolService;
import com.cloudok.base.vo.SchoolVO;
import com.cloudok.core.service.AbstractService;

import java.util.List;

@Service
public class SchoolServiceImpl extends AbstractService<SchoolVO, SchoolPO> implements SchoolService {

    private SchoolMapper repository;

    @Autowired
    public SchoolServiceImpl(SchoolMapper repository) {
        super(repository);
        this.repository = repository;
    }


    @Override
    public List<SchoolVO> listByArea(Long areaId) {
        List<SchoolVO> list = this.repository.listByArea(areaId);
        return list;
    }

    @Override
    public Page<SchoolVO> searchSchool(String keywords, Integer pageNo, Integer pageSize) {
        if (StringUtils.isEmpty(keywords) || StringUtils.isEmpty(keywords.trim())) {
            throw new SystemException(CloudOKExceptionMessage.SEARCH_KEYWORDS_IS_NULL);
        }
        if (keywords.trim().length() > 100) {// 不支持这么长的昵称 直接给他默认返回
            return new Page<SchoolVO>();
        }
        Page<SchoolVO> page = null;
        page = this.page(QueryBuilder.create(SchoolMapping.class).and(SchoolMapping.NAME, QueryOperator.LIKE, keywords.trim()).end().sort(SchoolMapping.SN)
                .asc().enablePaging().page(pageNo, pageSize).end());
        if (null == page.getData()) {
            page = this.page(QueryBuilder.create(SchoolMapping.class).and(SchoolMapping.ABBREVIATION, QueryOperator.LIKE, keywords.trim()).end().sort(SchoolMapping.SN)
                    .asc().enablePaging().page(pageNo, pageSize).end());
        }
        return page;
    }
}
