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
import com.cloudok.uc.mapping.EducationExperienceMapping;
import com.cloudok.uc.service.EducationExperienceService;
import com.cloudok.uc.vo.EducationExperienceVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppEducationExperienceApi")
@RequestMapping("/v1/uc/member/{memberId}/educationExperience")
@Api(tags = "教育经历")
public class EducationExperienceApi {

	@Autowired
	private EducationExperienceService educationExperienceService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加教育经历", notes = "添加教育经历")
	public Response create(@RequestBody @Valid EducationExperienceVO vo) {
		return Response.buildSuccess(educationExperienceService.create(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询教育经历列表", notes = "查询教育经历列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(educationExperienceService.page(QueryBuilder.create(EducationExperienceMapping.class).with(request)));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改教育经历", notes = "修改教育经历")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid EducationExperienceVO vo) {
		vo.setId(id);
		return Response.buildSuccess(educationExperienceService.update(vo));
	}
 
	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除教育经历", notes = "删除教育经历")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(educationExperienceService.remove(id));
	}
 
}