package com.cloudok.authority.api;

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

import com.cloudok.authority.mapping.ResourceMapping;
import com.cloudok.authority.service.ResourceService;
import com.cloudok.authority.vo.ResourceVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

@RestController
@RequestMapping("/v1/authority/resource")
@Api(tags = "")
@LogModule
public class ResourceApi {

	@Autowired
	private ResourceService resourceService;

	@PreAuthorize("hasAuthority('interface.resource.write')")
	@PostMapping
	@ApiOperation(value = "添加", notes = "添加")
	@Loggable
	public Response create(@RequestBody @Valid ResourceVO vo) {
		return Response.buildSuccess(resourceService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.resource.write') or hasAuthority('interface.resource.read')")
	@GetMapping
	@ApiOperation(value = "查询列表", notes = "查询列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(resourceService.page(QueryBuilder.create(ResourceMapping.class).with(request).sort(ResourceMapping.SN).asc()));
	}

	@PreAuthorize("hasAuthority('interface.resource.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "修改")
	@Loggable
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid ResourceVO vo) {
		vo.setId(id);
		return Response.buildSuccess(resourceService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.resource.write') or hasAuthority('interface.resource.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询", notes = "查询")
	@Loggable
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(resourceService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.resource.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除", notes = "删除")
	@Loggable
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(resourceService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.resource.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除", notes = "批量删除")
	@Loggable
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(resourceService.remove(ids));
	}
}
