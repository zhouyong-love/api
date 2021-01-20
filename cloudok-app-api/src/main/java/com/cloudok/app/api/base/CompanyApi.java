package com.cloudok.app.api.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.base.mapping.CompanyMapping;
import com.cloudok.base.service.CompanyService;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppCompanyApi")
@RequestMapping("/v1/base/company")
@Api(tags = "公司信息")
public class CompanyApi {

	@Autowired
	private CompanyService companyService;
 
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询公司信息列表", notes = "查询公司信息列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(companyService.page(QueryBuilder.create(CompanyMapping.class).with(request)));
	} 
}
