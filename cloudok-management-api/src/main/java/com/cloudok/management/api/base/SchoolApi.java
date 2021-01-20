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
import com.cloudok.base.mapping.SchoolMapping;
import com.cloudok.base.service.SchoolService;
import com.cloudok.base.vo.SchoolVO;

@RestController
@RequestMapping("/v1/management/base/school")
@Api(tags = "学校基础数据管理")
public class SchoolApi {

	@Autowired
	private SchoolService schoolService;

	@PreAuthorize("hasAuthority('interface.school.write')")
	@PostMapping
	@ApiOperation(value = "添加学校基础数据", notes = "添加学校基础数据")
	public Response create(@RequestBody @Valid SchoolVO vo) {
		return Response.buildSuccess(schoolService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.school.write') or hasAuthority('interface.school.read')")
	@GetMapping
	@ApiOperation(value = "查询学校基础数据列表", notes = "查询学校基础数据列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(schoolService.page(QueryBuilder.create(SchoolMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.school.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改学校基础数据", notes = "修改学校基础数据")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid SchoolVO vo) {
		vo.setId(id);
		return Response.buildSuccess(schoolService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.school.write') or hasAuthority('interface.school.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询学校基础数据", notes = "查询学校基础数据")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(schoolService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.school.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除学校基础数据", notes = "删除学校基础数据")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(schoolService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.school.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除学校基础数据", notes = "批量删除学校基础数据")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(schoolService.remove(ids));
	}
}
