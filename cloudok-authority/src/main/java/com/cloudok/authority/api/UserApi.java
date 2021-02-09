package com.cloudok.authority.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.authority.mapping.UserMapping;
import com.cloudok.authority.service.UserService;
import com.cloudok.authority.vo.UserVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/authority/user")
@Api(tags = "用户表")
@LogModule
public class UserApi {

	@Autowired
	private UserService userService;

	@PreAuthorize("hasAuthority('interface.user.write')")
	@PostMapping
	@ApiOperation(value = "添加用户表", notes = "添加用户表")
	@Loggable
	public Response create(@RequestBody @Valid UserVO vo) {
		return Response.buildSuccess(userService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.user.write') or hasAuthority('interface.user.read')")
	@GetMapping
	@ApiOperation(value = "查询用户表列表", notes = "查询用户表列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(userService.page(QueryBuilder.create(UserMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.user.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改用户表", notes = "修改用户表")
	@Loggable
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid UserVO vo) {
		vo.setId(id);
		return Response.buildSuccess(userService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.user.write') or hasAuthority('interface.user.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询用户表", notes = "查询用户表")
	@Loggable
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(userService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.user.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除用户表", notes = "删除用户表")
	@Loggable
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(userService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.user.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除用户表", notes = "批量删除用户表")
	@Loggable
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(userService.remove(ids));
	}
}
