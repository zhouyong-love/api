package com.cloudok.app.api.base;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.base.mapping.TagMapping;
import com.cloudok.base.service.TagService;
import com.cloudok.base.vo.TagVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppTagApi")
@RequestMapping("/v1/base/tag")
@Api(tags = "标签")
@LogModule
public class TagApi {

	@Autowired
	private TagService tagService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@Loggable
	@ApiOperation(value = "添加标签", notes = "添加标签")
	public Response create(@RequestBody @Valid TagVO vo) {
		return Response.buildSuccess(tagService.createByMember(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@Loggable
	@ApiOperation(value = "查询标签列表", notes = "查询标签列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(tagService.list(QueryBuilder.create(TagMapping.class).with(request).disenablePaging().sort(TagMapping.SN).asc()));
	}

//	@PreAuthorize("isFullyAuthenticated()")
//	@PutMapping("/{id}")
//	@ApiOperation(value = "修改标签", notes = "修改标签")
//	@Loggable
//	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid TagVO vo) {
//		vo.setId(id);
//		return Response.buildSuccess(tagService.updateByMember(vo));
//	}
//
//
//	@PreAuthorize("isFullyAuthenticated()")
//	@DeleteMapping("/{id}")
//	@ApiOperation(value = "删除标签", notes = "删除标签")
//	@Loggable
//	public Response remove(@PathVariable("id") Long id) {
//		return Response.buildSuccess(tagService.removeByMember(id));
//	}
// 
}
