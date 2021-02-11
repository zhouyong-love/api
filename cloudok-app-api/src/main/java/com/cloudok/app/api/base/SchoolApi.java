package com.cloudok.app.api.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.base.mapping.SchoolMapping;
import com.cloudok.base.service.SchoolService;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppSchoolApi")
@RequestMapping("/v1/base/school")
@Api(tags = "学校基础数据")
@LogModule
public class SchoolApi {

	@Autowired
	private SchoolService schoolService;

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询学校基础数据列表", notes = "查询学校基础数据列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(schoolService.list(QueryBuilder.create(SchoolMapping.class).with(request).sort(SchoolMapping.SN).asc()));
	}
}
