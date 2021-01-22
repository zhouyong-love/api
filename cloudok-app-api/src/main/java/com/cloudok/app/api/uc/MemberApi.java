package com.cloudok.app.api.uc;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.core.exception.SystemException;
import com.cloudok.core.vo.Response;
import com.cloudok.security.exception.SecurityExceptionMessage;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.vo.BindRequest;
import com.cloudok.uc.vo.ChangePasswordRequest;
import com.cloudok.uc.vo.ForgotVO;
import com.cloudok.uc.vo.LoginVO;
import com.cloudok.uc.vo.MemberVO;
import com.cloudok.uc.vo.SingupVO;
import com.cloudok.uc.vo.TokenVO;
import com.cloudok.uc.vo.UserCheckRequest;
import com.cloudok.uc.vo.VerifyCodeRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppMemberApi")
@RequestMapping("/v1/uc/member")
@Api(tags = "会员表")
public class MemberApi {

	@Autowired
	private MemberService memberService;

	@PostMapping("/register")
	@ApiOperation(value = "注册", notes = "注册")
	public Response create(@RequestBody @Valid SingupVO vo) {
		return Response.buildSuccess(memberService.signup(vo));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping("/fill")
	@ApiOperation(value = "补充账号消息", notes = "补充账号消息")
	public Response fillAccountInfo(@RequestBody @Valid MemberVO vo) {
		return Response.buildSuccess(memberService.fillAccountInfo(vo));
	}
	
	@PostMapping("/login")
	@ApiOperation(value = "user login", notes = "user login")
	public Response login(@RequestBody LoginVO vo) {
		try {
			return Response.buildSuccess(memberService.login(vo));
		}catch (Exception e) {
			if(e instanceof SystemException) {
				throw e;
			}
			return Response.buildFail(SecurityExceptionMessage.ACCESS_INCORRECT_CERTIFICATE);
		}
	}
	
	@PostMapping("/logout")
	@ApiOperation(value = "logout", notes = "logout")
	public Response logout() {
		return Response.buildSuccess(memberService.logout());
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/refreshToken")
	@ApiOperation(value="refresh token",notes="refresh token")
	public Response refreshToken(@RequestBody TokenVO vo) {
		return Response.buildSuccess(memberService.refreshToken(vo.getRefreshToken()));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/userInfo")
	@ApiOperation(value="get current user info",notes="get current user info")
	public Response userInfo() {
		return Response.buildSuccess(memberService.getCurrentUserInfo());
	}
	
	@PostMapping("/exists/username")
	@ApiOperation(value = "check user name exists or not", notes = "check user name exists or not")
	public Response checkUserName(@RequestBody UserCheckRequest request) {
		return Response.buildSuccess(memberService.checkUserName(request));
	}
	
	@PostMapping("/exists/email")
	@ApiOperation(value = "check user email exists or not", notes = "check user email exists or not")
	public Response checkEmail(@RequestBody UserCheckRequest request) {
		return Response.buildSuccess(memberService.checkEmail(request));
	}
	 
	
	@PostMapping("/verifycode")
	@ApiOperation(value = "verify code request", notes = "verify code request")
	public Response sendVerifycode(@RequestBody VerifyCodeRequest vo) {
		return Response.buildSuccess(memberService.sendVerifycode(vo));
	}
	 
	@PostMapping("/signup")
	@ApiOperation(value = "user register", notes = "user register")
	public Response signup(@RequestBody SingupVO vo) {
			return Response.buildSuccess(memberService.signup(vo));
	}
	
	@PostMapping("/reset")
	@ApiOperation(value = "user reset password", notes = "user reset password")
	public Response resetPwd(@RequestBody ForgotVO vo) {
			return Response.buildSuccess(memberService.resetPwd(vo));
	}

	@PostMapping("/changePassword")
	@ApiOperation(value = "user register", notes = "user register")
	public Response changePassword(@RequestBody ChangePasswordRequest vo) {
			return Response.buildSuccess(memberService.changePassword(vo));
	}
	

	@PostMapping("/bind")
	@ApiOperation(value = "user register", notes = "user register")
	public Response bindEmailOrPhone(@RequestBody BindRequest vo) {
			return Response.buildSuccess(memberService.bind(vo));
	}
	
 
}
