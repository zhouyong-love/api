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
import com.cloudok.uc.service.ResearchExperienceService;
import com.cloudok.uc.vo.ResearchExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppResearchExperienceApi")
@RequestMapping("/v1/uc/research")
@Api(tags = "科研经历")
@LogModule
public class ResearchExperienceApi {

	@Autowired
	private ResearchExperienceService researchExperienceService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加科研经历", notes = "添加科研经历")
	@Loggable
	public Response create(@RequestBody @Valid ResearchExperienceVO vo) {
		return Response.buildSuccess(researchExperienceService.create(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询科研经历列表", notes = "查询科研经历列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(researchExperienceService.getByMember(SecurityContextHelper.getCurrentUserId()));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改科研经历", notes = "修改科研经历")
	@Loggable
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid ResearchExperienceVO vo) {
		vo.setId(id);
		return Response.buildSuccess(researchExperienceService.update(vo));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询科研经历", notes = "查询科研经历")
	@Loggable
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(researchExperienceService.getByMember(SecurityContextHelper.getCurrentUserId(),id));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除科研经历", notes = "删除科研经历")
	@Loggable
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(researchExperienceService.remove(id));
	}
 

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/switchSN")
	@Loggable
	@ApiOperation(value = "调整排序", notes = "调整排序")
	public Response switchSN(@RequestBody @Valid SwitchSNRequest switchSNRequest) {
		return Response.buildSuccess(researchExperienceService.switchSN(switchSNRequest));
	}
}
