package com.cloudok.app.api.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.base.mapping.SpecialismMapping;
import com.cloudok.base.service.SpecialismService;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppSpecialismApi")
@RequestMapping("/v1/base/specialism")
@Api(tags = "专业")
public class SpecialismApi {

	@Autowired
	private SpecialismService specialismService;

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询专业列表", notes = "查询专业列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(specialismService.page(QueryBuilder.create(SpecialismMapping.class).with(request)));
	} 
}
