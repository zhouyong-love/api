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

import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.uc.mapping.InternshipExperienceMapping;
import com.cloudok.uc.service.InternshipExperienceService;
import com.cloudok.uc.vo.InternshipExperienceVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppInternshipExperienceApi")
@RequestMapping("/v1/uc/member/{memberId}/internshipExperience")
@Api(tags = "实习经历")
public class InternshipExperienceApi {

	@Autowired
	private InternshipExperienceService internshipExperienceService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加实习经历", notes = "添加实习经历")
	public Response create(@RequestBody @Valid InternshipExperienceVO vo) {
		return Response.buildSuccess(internshipExperienceService.create(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询实习经历列表", notes = "查询实习经历列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(internshipExperienceService.page(QueryBuilder.create(InternshipExperienceMapping.class).with(request)));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改实习经历", notes = "修改实习经历")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid InternshipExperienceVO vo) {
		vo.setId(id);
		return Response.buildSuccess(internshipExperienceService.update(vo));
	}
 

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除实习经历", notes = "删除实习经历")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(internshipExperienceService.remove(id));
	}
 
}