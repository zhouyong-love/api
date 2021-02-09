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

import com.cloudok.authority.mapping.RoleMapping;
import com.cloudok.authority.mapping.RoleResMapping;
import com.cloudok.authority.service.RoleResService;
import com.cloudok.authority.service.RoleService;
import com.cloudok.authority.vo.RoleResVO;
import com.cloudok.authority.vo.RoleVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

@RestController
@RequestMapping("/v1/authority/role")
@Api(tags = "")
@LogModule
public class RoleApi {

	@Autowired
	private RoleService roleService;

	@PreAuthorize("hasAuthority('interface.role.write')")
	@PostMapping
	@ApiOperation(value = "添加", notes = "添加")
	@Loggable
	public Response create(@RequestBody @Valid RoleVO vo) {
		return Response.buildSuccess(roleService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.role.write') or hasAuthority('interface.role.read')")
	@GetMapping
	@ApiOperation(value = "查询列表", notes = "查询列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(roleService.page(QueryBuilder.create(RoleMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.role.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改", notes = "修改")
	@Loggable
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid RoleVO vo) {
		vo.setId(id);
		return Response.buildSuccess(roleService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.role.write') or hasAuthority('interface.role.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询", notes = "查询")
	@Loggable
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(roleService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.role.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除", notes = "删除")
	@Loggable
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(roleService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.role.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除", notes = "批量删除")
	@Loggable
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(roleService.remove(ids));
	}
	
	@Autowired
	private RoleResService roleResServer;
	
	@PostMapping("/{roleId}/resources")
	@PreAuthorize("hasAuthority('role.write')")
	@ApiOperation(value="为角色授予资源权限",notes="为角色授予资源权限")
	@Loggable
	public Response grant(@RequestBody List<RoleResVO> vos,@PathVariable("roleId")Long roleId) {
		for (RoleResVO roleResVO : vos) {
			roleResVO.setRoleId(roleId);
		}
		return Response.buildSuccess(roleResServer.create(vos));
	}
	
	@DeleteMapping("/{roleId}/resources/{resourcesId}")
	@PreAuthorize("hasAuthority('role.write')")
	@ApiOperation(value="删除角色的资源权限",notes="删除角色的资源权限")
	@Loggable
	public Response ungrant(@PathVariable("resourcesId")Long id) {
		return Response.buildSuccess(roleResServer.remove(id));
	}
	
	@GetMapping("/{roleId}/resources")
	@PreAuthorize("hasAuthority('role.read') or hasAuthority('role.write')")
	@ApiOperation(value="查询角色的权限列表",notes="查询角色的权限列表")
	@Loggable
	public Response roleResList(@PathVariable("roleId")Long roleId) {
		return Response.buildSuccess(roleResServer.list(QueryBuilder.create(RoleResMapping.class).and(RoleResMapping.ROLEID,roleId).end()));
	} 
}
