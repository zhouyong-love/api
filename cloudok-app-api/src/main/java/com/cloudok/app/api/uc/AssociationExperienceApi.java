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
import com.cloudok.uc.mapping.AssociationExperienceMapping;
import com.cloudok.uc.service.AssociationExperienceService;
import com.cloudok.uc.vo.AssociationExperienceVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppAssociationExperienceApi")
@RequestMapping("/v1/uc/member/{memberId}/associationExperience")
@Api(tags = "社团经历")
public class AssociationExperienceApi {

	@Autowired
	private AssociationExperienceService associationExperienceService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加社团经历", notes = "添加社团经历")
	public Response create(@RequestBody @Valid AssociationExperienceVO vo) {
		return Response.buildSuccess(associationExperienceService.create(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询社团经历列表", notes = "查询社团经历列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(associationExperienceService.page(QueryBuilder.create(AssociationExperienceMapping.class).with(request)));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改社团经历", notes = "修改社团经历")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid AssociationExperienceVO vo) {
		vo.setId(id);
		return Response.buildSuccess(associationExperienceService.update(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除社团经历", notes = "删除社团经历")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(associationExperienceService.remove(id));
	}

}