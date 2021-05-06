package com.cloudok.app.api.base;

import com.cloudok.base.mapping.AreaMapping;
import com.cloudok.base.mapping.SchoolMapping;
import com.cloudok.base.service.AreaService;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author zhouyong
 * @Date 2021/5/4 0004 1:25
 * @Version 1.0
 */
@RestController("AppAreaApi")
@RequestMapping("/v1/base/area")
@Api(tags = "学校地区数据")
@LogModule
public class AreaApi {

    @Autowired
    private AreaService areaService;

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("/getAreaBySchool")
    @ApiOperation(value = "查询学校地区学校基础数据列表", notes = "查询学校地区学校基础数据列表")
    @Loggable
    public Response listAreaBySchool(HttpServletRequest request) {
        return Response.buildSuccess(areaService.listAreaBySchool(request));
    }
}
