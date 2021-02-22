package com.cloudok.app.api.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.base.mapping.IndustryMapping;
import com.cloudok.base.service.IndustryService;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppIndustryApi")
@RequestMapping("/v1/base/industry")
@Api(tags = "行业信息")
@LogModule
public class IndustryApi {

	@Autowired
	private IndustryService industryService;
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询行业信息列表", notes = "查询行业信息列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(industryService.list(QueryBuilder.create(IndustryMapping.class).with(request).disenablePaging().sort(IndustryMapping.SN).asc()));
	} 
}
