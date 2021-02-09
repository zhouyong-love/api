package com.cloudok.authority.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.authority.service.AuthService;
import com.cloudok.authority.service.ResourceService;
import com.cloudok.authority.service.UserService;
import com.cloudok.authority.vo.LoginVO;
import com.cloudok.authority.vo.TokenVO;
import com.cloudok.authority.vo.UserVO;
import com.cloudok.base.attach.io.AttachRWHandle;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.security.exception.SecurityExceptionMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月18日 下午12:10:27
 */
@RestController
@RequestMapping("/v1/authority")
@Api(tags = "")
@LogModule
public class AuthApi {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ResourceService resourceService;
	
	@PostMapping("/login")
	@Loggable
	@ApiOperation(value = "用户登录", notes = "用户登录")
	public Response create(@RequestBody LoginVO vo) {
		try {
			return Response.buildSuccess(authService.login(vo));
		}catch (Exception e) {
			e.printStackTrace();
			return Response.buildFail(SecurityExceptionMessage.ACCESS_INCORRECT_CERTIFICATE);
		}
	}
	
	@PostMapping("/logout")
	@ApiOperation(value = "用户登出", notes = "用户登出")
	@Loggable
	public Response logout() {
		authService.logout();
		return Response.buildSuccess();
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/refreshToken")
	@ApiOperation(value="刷新token",notes="刷新token")
	public Response refreshToken(@RequestBody TokenVO vo) {
		return Response.buildSuccess(authService.refreshToken(vo.getRefreshToken()));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/provisionalPass")
	@ApiOperation(value="获取临时通行令牌",notes="获取临时通行令牌")
	@Loggable
	public Response provisionalPass() {
		return Response.buildSuccess(authService.provisionalPass());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/userInfo")
	@ApiOperation(value="获取登录用户信息",notes="获取登录用户信息")
	@Loggable
	public Response userInfo() {
		return Response.buildSuccess(authService.userInfo());
	}
	
	@PutMapping("/userInfo")
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value = "修改用户", notes = "修改用户")
	@Loggable
	public Response set(@RequestBody UserVO vo) {
		vo.setId(SecurityContextHelper.getCurrentUserId());
		if (!StringUtils.isEmpty(vo.getPassword())) {
			vo.setPassword(passwordEncoder.encode(vo.getPassword()));
		}
		if (vo.getAvatar() != null) {
			AttachRWHandle.used(vo.getAvatar(), vo.getId(), null);
		}
		vo.setEmail(null);
		vo.setTelphone(null);
		vo.setFreeze(null);
		vo.setUserName(null);
		return Response.buildSuccess(userService.merge(vo));
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/menu")
	@ApiOperation(value="用户菜单",notes="用户菜单")
	@Loggable
	public Response menu() {
		return Response.buildSuccess(resourceService.getMenu(SecurityContextHelper.getCurrentUserId()));
	}
}
