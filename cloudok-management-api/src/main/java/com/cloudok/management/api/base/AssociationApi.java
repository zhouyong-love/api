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
import com.cloudok.base.mapping.AssociationMapping;
import com.cloudok.base.service.AssociationService;
import com.cloudok.base.vo.AssociationVO;

@RestController
@RequestMapping("/v1/management/base/association")
@Api(tags = "社团数据管理")
public class AssociationApi {

	@Autowired
	private AssociationService associationService;

	@PreAuthorize("hasAuthority('interface.association.write')")
	@PostMapping
	@ApiOperation(value = "添加社团数据", notes = "添加社团数据")
	public Response create(@RequestBody @Valid AssociationVO vo) {
		return Response.buildSuccess(associationService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.association.write') or hasAuthority('interface.association.read')")
	@GetMapping
	@ApiOperation(value = "查询社团数据列表", notes = "查询社团数据列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(associationService.page(QueryBuilder.create(AssociationMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.association.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改社团数据", notes = "修改社团数据")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid AssociationVO vo) {
		vo.setId(id);
		return Response.buildSuccess(associationService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.association.write') or hasAuthority('interface.association.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询社团数据", notes = "查询社团数据")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(associationService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.association.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除社团数据", notes = "删除社团数据")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(associationService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.association.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除社团数据", notes = "批量删除社团数据")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(associationService.remove(ids));
	}
}
