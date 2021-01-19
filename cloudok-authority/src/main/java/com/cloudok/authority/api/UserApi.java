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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.cloudok.authority.mapping.UserMapping;
import com.cloudok.authority.service.UserService;
import com.cloudok.authority.service.impl.UserRoleGrantHandler;
import com.cloudok.authority.vo.UserVO;
import com.cloudok.base.attach.io.AttachRWHandle;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Page;
import com.cloudok.core.vo.Response;

@RestController
@RequestMapping("/v1/authority/user")
@Api(tags = "user table")
public class UserApi {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserRoleGrantHandler grantHandler;
	
	@PreAuthorize("hasAuthority('interface.user.write')")
	@PostMapping
	@ApiOperation(value = "添加user table", notes = "添加user table")
	public Response create(@RequestBody @Valid UserVO vo) {
		vo.setPasswdHash(passwordEncoder.encode(vo.getPasswdHash()));
		UserVO user = userService.create(vo);
		if (vo.getAvatar() != null) {
			AttachRWHandle.used(vo.getAvatar(), vo.getId(), null);
		}
		return Response.buildSuccess(user);
	}

	@PreAuthorize("hasAuthority('interface.user.write') or hasAuthority('interface.user.read')")
	@GetMapping
	@ApiOperation(value = "查询user table列表", notes = "查询user table列表")
	public Response search(HttpServletRequest request) {
		Page<UserVO> userPage = userService.page(QueryBuilder.create(UserMapping.class).with(request));
		if (userPage.getData() != null && userPage.getData().size() > 0) {
			userPage.getData().forEach(u -> {
				u.setPasswdHash(null);
			});
		}
		return Response.buildSuccess(userPage);
	}

	@GetMapping("/exists/{username}")
	@ApiOperation(value = "查询user是否存在", notes = "查询user是否存在")
	public Response exists(@PathVariable("username") String username) {
		return Response.buildSuccess(userService.exists(username));
	}

	@PreAuthorize("hasAuthority('interface.user.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改user table", notes = "修改user table")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid UserVO vo) {
		vo.setId(id);
		vo.setUserName(null);
		if (!StringUtils.isEmpty(vo.getPasswdHash())) {
			vo.setPasswdHash(passwordEncoder.encode(vo.getPasswdHash()));
		}
		if (vo.getAvatar() != null) {
			AttachRWHandle.used(vo.getAvatar(), vo.getId(), null);
		}
		return Response.buildSuccess(userService.merge(vo));
	}

	@PreAuthorize("hasAuthority('interface.user.write') or hasAuthority('interface.user.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询user table", notes = "查询user table")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(userService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.user.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除user table", notes = "删除user table")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(userService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.user.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除user table", notes = "批量删除user table")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(userService.remove(ids));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}/roles")
	@ApiOperation(value = "加载用户拥有的角色", notes = "加载用户拥有的角色")
	public Response userRoles(@PathVariable("id") Long id) {
		return Response.buildSuccess(grantHandler.getObjectRoles(id));
	}
}
