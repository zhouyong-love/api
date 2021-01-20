package com.cloudok.management.api.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.base.mapping.TagMapping;
import com.cloudok.base.service.TagService;
import com.cloudok.base.vo.TagVO;

@RestController
@RequestMapping("/v1/management/base/tag")
@Api(tags = "标签管理")
public class TagApi {

	@Autowired
	private TagService tagService;

	@PreAuthorize("hasAuthority('interface.tag.write')")
	@PostMapping
	@ApiOperation(value = "添加标签", notes = "添加标签")
	public Response create(@RequestBody @Valid TagVO vo) {
		return Response.buildSuccess(tagService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.tag.write') or hasAuthority('interface.tag.read')")
	@GetMapping
	@ApiOperation(value = "查询标签列表", notes = "查询标签列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(tagService.page(QueryBuilder.create(TagMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.tag.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改标签", notes = "修改标签")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid TagVO vo) {
		vo.setId(id);
		return Response.buildSuccess(tagService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.tag.write') or hasAuthority('interface.tag.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询标签", notes = "查询标签")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(tagService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.tag.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除标签", notes = "删除标签")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(tagService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.tag.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除标签", notes = "批量删除标签")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(tagService.remove(ids));
	}
}
