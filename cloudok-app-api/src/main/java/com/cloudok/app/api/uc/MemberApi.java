package com.cloudok.app.api.uc;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.bbs.service.CollectService;
import com.cloudok.bbs.service.PostService;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.security.exception.SecurityExceptionMessage;
import com.cloudok.uc.mapping.MemberMapping;
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

	@Autowired
	private PostService postService;
	
	@Autowired
	private CollectService collectService;
	
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
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/fullUserInfo")
	@ApiOperation(value="get current full user info",notes="get current full user info")
	public Response fullUserInfo() {
		return Response.buildSuccess(memberService.getWholeMemberInfo(SecurityContextHelper.getCurrentUserId()));
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
	
	@PostMapping("/exists/phone")
	@ApiOperation(value = "check user phone exists or not", notes = "check user phone exists or not")
	public Response checkPhone(@RequestBody UserCheckRequest request) {
		return Response.buildSuccess(memberService.checkPhone(request));
	}
	 
	
	@PostMapping("/verifycode")
	@ApiOperation(value = "verify code request", notes = "verify code request")
	public Response sendVerifycode(@RequestBody VerifyCodeRequest vo) {
		return Response.buildSuccess(memberService.sendVerifycode(vo));
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
	
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{memberId}/bbs")
	@ApiOperation(value = "查询某一个人的动态列表", notes = "查询某一个人的动态列表")
	public Response search(@PathVariable("memberId") Long memberId,HttpServletRequest request) {
		QueryBuilder query = QueryBuilder.create(com.cloudok.bbs.mapping.PostMapping.class).with(request);
		query.and(com.cloudok.bbs.mapping.PostMapping.CREATEBY,  memberId);
		return Response.buildSuccess(postService.page(query));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/collect/posts")
	@ApiOperation(value = "我收藏的动态", notes = "我收藏的动态")
	public Response getMyCollects(@RequestParam(name = "pageNo",defaultValue="1") Integer pageNo,
			@RequestParam(name = "pageSize",defaultValue="10") Integer pageSize) {
		return Response.buildSuccess(collectService.getMyCollectPosts(SecurityContextHelper.getCurrentUserId(),pageNo,pageSize));
	}
 
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/link")
	@ApiOperation(value="查询关联用户详细信息",notes="查询关联用户详细信息")
	public Response link(HttpServletRequest request) {
		return Response.buildSuccess(memberService.link(QueryBuilder.create(MemberMapping.class).with(request)));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/simpleInfo")
	@ApiOperation(value="查询登录用户的简要信息",notes="查询登录用户的简要信息")
	public Response simpleInfo() {
		return Response.buildSuccess(memberService.getSimpleMemberInfo());
	}
}
