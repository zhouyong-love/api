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
import com.cloudok.base.mapping.CompanyMapping;
import com.cloudok.base.service.CompanyService;
import com.cloudok.base.vo.CompanyVO;

@RestController
@RequestMapping("/v1/management/base/company")
@Api(tags = "公司信息管理")
public class CompanyApi {

	@Autowired
	private CompanyService companyService;

	@PreAuthorize("hasAuthority('interface.company.write')")
	@PostMapping
	@ApiOperation(value = "添加公司信息", notes = "添加公司信息")
	public Response create(@RequestBody @Valid CompanyVO vo) {
		return Response.buildSuccess(companyService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.company.write') or hasAuthority('interface.company.read')")
	@GetMapping
	@ApiOperation(value = "查询公司信息列表", notes = "查询公司信息列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(companyService.page(QueryBuilder.create(CompanyMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.company.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改公司信息", notes = "修改公司信息")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid CompanyVO vo) {
		vo.setId(id);
		return Response.buildSuccess(companyService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.company.write') or hasAuthority('interface.company.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询公司信息", notes = "查询公司信息")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(companyService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.company.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除公司信息", notes = "删除公司信息")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(companyService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.company.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除公司信息", notes = "批量删除公司信息")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(companyService.remove(ids));
	}
}
