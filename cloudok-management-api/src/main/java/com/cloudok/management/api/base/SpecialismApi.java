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
import com.cloudok.base.mapping.SpecialismMapping;
import com.cloudok.base.service.SpecialismService;
import com.cloudok.base.vo.SpecialismVO;

@RestController
@RequestMapping("/v1/management/base/specialism")
@Api(tags = "专业管理")
public class SpecialismApi {

	@Autowired
	private SpecialismService specialismService;

	@PreAuthorize("hasAuthority('interface.specialism.write')")
	@PostMapping
	@ApiOperation(value = "添加专业", notes = "添加专业")
	public Response create(@RequestBody @Valid SpecialismVO vo) {
		return Response.buildSuccess(specialismService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.specialism.write') or hasAuthority('interface.specialism.read')")
	@GetMapping
	@ApiOperation(value = "查询专业列表", notes = "查询专业列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(specialismService.page(QueryBuilder.create(SpecialismMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.specialism.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改专业", notes = "修改专业")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid SpecialismVO vo) {
		vo.setId(id);
		return Response.buildSuccess(specialismService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.specialism.write') or hasAuthority('interface.specialism.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询专业", notes = "查询专业")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(specialismService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.specialism.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除专业", notes = "删除专业")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(specialismService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.specialism.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除专业", notes = "批量删除专业")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(specialismService.remove(ids));
	}
}
