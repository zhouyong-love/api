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
import com.cloudok.uc.service.InternshipExperienceService;
import com.cloudok.uc.vo.InternshipExperienceVO;
import com.cloudok.uc.vo.SwitchSNRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppInternshipExperienceApi")
@RequestMapping("/v1/uc/internshipExperience")
@Api(tags = "实习经历")
@LogModule
public class InternshipExperienceApi {

	@Autowired
	private InternshipExperienceService internshipExperienceService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加实习经历", notes = "添加实习经历")
	@Loggable
	public Response create(@RequestBody @Valid InternshipExperienceVO vo) {
		return Response.buildSuccess(internshipExperienceService.create(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询实习经历列表", notes = "查询实习经历列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(internshipExperienceService.getByMember(SecurityContextHelper.getCurrentUserId()));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改实习经历", notes = "修改实习经历")
	@Loggable
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid InternshipExperienceVO vo) {
		vo.setId(id);
		return Response.buildSuccess(internshipExperienceService.update(vo));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{id}")
	@ApiOperation(value = "获取实习经历", notes = "获取实习经历")
	@Loggable
	public Response get(@PathVariable("id") Long id) {
		return Response.buildSuccess(internshipExperienceService.getByMember(SecurityContextHelper.getCurrentUserId(),id));
	}
 

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除实习经历", notes = "删除实习经历")
	@Loggable
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(internshipExperienceService.remove(id));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/switchSN")
	@Loggable
	@ApiOperation(value = "调整排序", notes = "调整排序")
	public Response switchSN(@RequestBody @Valid SwitchSNRequest switchSNRequest) {
		return Response.buildSuccess(internshipExperienceService.switchSN(switchSNRequest));
	}
 
}
