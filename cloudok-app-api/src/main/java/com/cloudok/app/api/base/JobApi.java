package com.cloudok.app.api.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.base.mapping.JobMapping;
import com.cloudok.base.service.JobService;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppJobApi")
@RequestMapping("/v1/base/job")
@Api(tags = "岗位")
@LogModule
public class JobApi {

	@Autowired
	private JobService jobService;

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询岗位列表", notes = "查询岗位列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(jobService.page(QueryBuilder.create(JobMapping.class).with(request).sort(JobMapping.SN).asc()));
	}
 
}
