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
import com.cloudok.base.mapping.IndustryMapping;
import com.cloudok.base.service.IndustryService;
import com.cloudok.base.vo.IndustryVO;

@RestController
@RequestMapping("/v1/management/base/industry")
@Api(tags = "行业管理")
public class IndustryApi {

	@Autowired
	private IndustryService industryService;

	@PreAuthorize("hasAuthority('interface.industry.write')")
	@PostMapping
	@ApiOperation(value = "添加行业信息", notes = "添加行业信息")
	public Response create(@RequestBody @Valid IndustryVO vo) {
		return Response.buildSuccess(industryService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.industry.write') or hasAuthority('interface.industry.read')")
	@GetMapping
	@ApiOperation(value = "查询行业信息列表", notes = "查询行业信息列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(industryService.page(QueryBuilder.create(IndustryMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.industry.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改行业信息", notes = "修改行业信息")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid IndustryVO vo) {
		vo.setId(id);
		return Response.buildSuccess(industryService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.industry.write') or hasAuthority('interface.industry.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询行业信息", notes = "查询行业信息")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(industryService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.industry.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除行业信息", notes = "删除行业信息")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(industryService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.industry.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除行业信息", notes = "批量删除行业信息")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(industryService.remove(ids));
	}
}
