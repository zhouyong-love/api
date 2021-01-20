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
import com.cloudok.base.mapping.JobMapping;
import com.cloudok.base.service.JobService;
import com.cloudok.base.vo.JobVO;

@RestController
@RequestMapping("/v1/management/base/job")
@Api(tags = "岗位管理")
public class JobApi {

	@Autowired
	private JobService jobService;

	@PreAuthorize("hasAuthority('interface.job.write')")
	@PostMapping
	@ApiOperation(value = "添加岗位", notes = "添加岗位")
	public Response create(@RequestBody @Valid JobVO vo) {
		return Response.buildSuccess(jobService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.job.write') or hasAuthority('interface.job.read')")
	@GetMapping
	@ApiOperation(value = "查询岗位列表", notes = "查询岗位列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(jobService.page(QueryBuilder.create(JobMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.job.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改岗位", notes = "修改岗位")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid JobVO vo) {
		vo.setId(id);
		return Response.buildSuccess(jobService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.job.write') or hasAuthority('interface.job.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询岗位", notes = "查询岗位")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(jobService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.job.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除岗位", notes = "删除岗位")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(jobService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.job.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除岗位", notes = "批量删除岗位")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(jobService.remove(ids));
	}
}
