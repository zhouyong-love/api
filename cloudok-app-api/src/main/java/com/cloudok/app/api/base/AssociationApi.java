package com.cloudok.app.api.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.base.mapping.AssociationMapping;
import com.cloudok.base.service.AssociationService;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppAssociationApi")
@RequestMapping("/v1/base/association")
@Api(tags = "社团数据")
@LogModule
public class AssociationApi {

	@Autowired
	private AssociationService associationService;

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询社团数据列表", notes = "查询社团数据列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(associationService.page(QueryBuilder.create(AssociationMapping.class).with(request).sort(AssociationMapping.SN).asc()));
	} 
}
