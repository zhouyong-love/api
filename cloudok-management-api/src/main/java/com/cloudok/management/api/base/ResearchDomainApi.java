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
import com.cloudok.base.mapping.ResearchDomainMapping;
import com.cloudok.base.service.ResearchDomainService;
import com.cloudok.base.vo.ResearchDomainVO;

@RestController
@RequestMapping("/v1/management/base/researchDomain")
@Api(tags = "研究领域管理")
public class ResearchDomainApi {

	@Autowired
	private ResearchDomainService researchDomainService;

	@PreAuthorize("hasAuthority('interface.researchDomain.write')")
	@PostMapping
	@ApiOperation(value = "添加研究领域", notes = "添加研究领域")
	public Response create(@RequestBody @Valid ResearchDomainVO vo) {
		return Response.buildSuccess(researchDomainService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.researchDomain.write') or hasAuthority('interface.researchDomain.read')")
	@GetMapping
	@ApiOperation(value = "查询研究领域列表", notes = "查询研究领域列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(researchDomainService.page(QueryBuilder.create(ResearchDomainMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.researchDomain.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改研究领域", notes = "修改研究领域")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid ResearchDomainVO vo) {
		vo.setId(id);
		return Response.buildSuccess(researchDomainService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.researchDomain.write') or hasAuthority('interface.researchDomain.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询研究领域", notes = "查询研究领域")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(researchDomainService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.researchDomain.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除研究领域", notes = "删除研究领域")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(researchDomainService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.researchDomain.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除研究领域", notes = "批量删除研究领域")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(researchDomainService.remove(ids));
	}
}
