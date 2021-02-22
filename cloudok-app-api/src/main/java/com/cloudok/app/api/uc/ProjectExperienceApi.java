package com.cloudok.app.api.uc;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.service.ProjectExperienceService;
import com.cloudok.uc.vo.ProjectExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppProjectExperienceApi")
@RequestMapping("/v1/uc/project")
@Api(tags = "项目实践")
@LogModule
public class ProjectExperienceApi {

	@Autowired
	private ProjectExperienceService projectExperienceService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加项目实践", notes = "添加项目实践")
	@Loggable
	public Response create(@RequestBody @Valid ProjectExperienceVO vo) {
		return Response.buildSuccess(projectExperienceService.create(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询项目实践列表", notes = "查询项目实践列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(projectExperienceService.getByMember(SecurityContextHelper.getCurrentUserId()));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询项目实践列表", notes = "查询项目实践列表")
	@Loggable
	public Response get(@PathVariable("id")Long id) {
		return Response.buildSuccess(projectExperienceService.getByMember(SecurityContextHelper.getCurrentUserId(),id));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改项目实践", notes = "修改项目实践")
	@Loggable
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid ProjectExperienceVO vo) {
		vo.setId(id);
		return Response.buildSuccess(projectExperienceService.update(vo));
	}
 
	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除项目实践", notes = "删除项目实践")
	@Loggable
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(projectExperienceService.remove(id));
	}
 

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/switchSN")
	@Loggable
	@ApiOperation(value = "调整排序", notes = "调整排序")
	public Response switchSN(@RequestBody @Valid SwitchSNRequest switchSNRequest) {
		return Response.buildSuccess(projectExperienceService.switchSN(switchSNRequest));
	}
}
