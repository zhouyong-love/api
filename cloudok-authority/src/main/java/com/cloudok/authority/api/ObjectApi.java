package com.cloudok.authority.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.cloudok.authority.service.ObjRoleService;
import com.cloudok.authority.service.grant.RoleGrantAdapter;
import com.cloudok.authority.vo.ObjRoleVO;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

@RestController
@RequestMapping("/v1/authority/object")
@LogModule
@Api(tags = "角色载体")
public class ObjectApi {

	@Autowired
	private RoleGrantAdapter roleGrantAdapter;
	
	@Autowired
	private ObjRoleService objectRoleServer;

	@PreAuthorize("hasAuthority('interface.role.read') or hasAuthority('interface.role.write')")
	@GetMapping("/types")
	@ApiOperation(value = "获取所有的角色载体类型", notes = "获取所有的角色载体类型")
	@Loggable
	public Response objectTypes() {
		return Response.buildSuccess(roleGrantAdapter.getConfiguer());
	}
	
	@PreAuthorize("hasAuthority('interface.role.read') or hasAuthority('interface.role.write')")
	@GetMapping("/{type}/list")
	@ApiOperation(value = "分页查询角色载体列表", notes = "分页查询角色载体列表")
	@Loggable
	public Response objects(@PathVariable("type")String type, @RequestParam("pageNo")int pageNo,@RequestParam("pageSize")int pageSize,@RequestParam("roleId")Long roleId,@RequestParam("keyWord")String keyWord) {
		return Response.buildSuccess(roleGrantAdapter.getObjects(type, keyWord, pageNo, pageSize,roleId));
	} 
	
	@PreAuthorize("hasAuthority('interface.role.write')")
	@PostMapping
	@ApiOperation(value = "为角色载体授予角色权限", notes = "为角色载体授予角色权限")
	@Loggable
	public Response add(@RequestBody ObjRoleVO roleVO) {
		return Response.buildSuccess(objectRoleServer.create(roleVO));
	}
	
	@PreAuthorize("hasAuthority('interface.role.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "解除角色载体的角色授权", notes = "解除角色载体的角色授权")
	@Loggable
	public Response remove(@PathVariable("id")Long id) {
		return Response.buildSuccess(objectRoleServer.remove(id));
	}
}
