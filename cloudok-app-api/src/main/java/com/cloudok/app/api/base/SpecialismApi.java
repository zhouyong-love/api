package com.cloudok.app.api.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.base.service.SpecialismService;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppSpecialismApi")
@RequestMapping("/v1/base/specialism")
@Api(tags = "专业")
@LogModule
public class SpecialismApi {

	@Autowired
	private SpecialismService specialismService;

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{schoolId}/school")
	@ApiOperation(value = "查询专业列表", notes = "查询专业列表")
	@Loggable
	public Response search(@PathVariable("schoolId")Long schoolId) {
		return Response.buildSuccess(specialismService.listBySchool(schoolId));
	} 
}
