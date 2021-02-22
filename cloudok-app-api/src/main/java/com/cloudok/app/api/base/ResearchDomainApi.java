package com.cloudok.app.api.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.base.mapping.ResearchDomainMapping;
import com.cloudok.base.service.ResearchDomainService;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppResearchDomainApi")
@RequestMapping("/v1/base/researchDomain")
@Api(tags = "研究领域")
@LogModule
public class ResearchDomainApi {

	@Autowired
	private ResearchDomainService researchDomainService;

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询研究领域列表", notes = "查询研究领域列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(researchDomainService.page(QueryBuilder.create(ResearchDomainMapping.class).with(request).sort(ResearchDomainMapping.SN).asc()));
	} 
}
