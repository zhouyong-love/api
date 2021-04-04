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
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.security.exception.SecurityExceptionMessage;
import com.cloudok.uc.dto.SimpleMemberDTO;
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
@LogModule
public class MemberApi {

	@Autowired
	private MemberService memberService;

	@Autowired
	private PostService postService;
	
	@Autowired
	private CollectService collectService;
	
	@Deprecated
	@PostMapping("/register")
	@ApiOperation(value = "注册", notes = "注册")
	@Loggable
	public Response create(@RequestBody @Valid SingupVO vo) {
		return Response.buildSuccess(memberService.signup(vo));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping("/fill")
	@ApiOperation(value = "补充账号消息", notes = "补充账号消息")
	@Loggable
	public Response fillAccountInfo(@RequestBody @Valid MemberVO vo) {
		return Response.buildSuccess(memberService.fillAccountInfo(vo));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping("/merge")
	@ApiOperation(value = "补充账号消息", notes = "补充账号消息")
	@Loggable
	public Response merge(@RequestBody @Valid MemberVO vo) {
		vo.setId(SecurityContextHelper.getCurrentUserId());
		return Response.buildSuccess(memberService.merge(vo));
	}
	
	@PostMapping("/login")
	@ApiOperation(value = "user login", notes = "user login")
	@Loggable
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
	@Loggable
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
	@Loggable
	public Response userInfo() {
		return Response.buildSuccess(memberService.getCurrentUserInfo());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/fullUserInfo")
	@ApiOperation(value="get current full user info",notes="get current full user info")
	@Loggable
	public Response fullUserInfo() {
		return Response.buildSuccess(memberService.getWholeMemberInfo(SecurityContextHelper.getCurrentUserId(),true));
	}
	@Deprecated
	@PostMapping("/exists/username")
	@ApiOperation(value = "check user name exists or not", notes = "check user name exists or not")
	public Response checkUserName(@RequestBody UserCheckRequest request) {
		return Response.buildSuccess(memberService.checkUserName(request));
	}
	@Deprecated
	@PostMapping("/exists/email")
	@ApiOperation(value = "check user email exists or not", notes = "check user email exists or not")
	public Response checkEmail(@RequestBody UserCheckRequest request) {
		return Response.buildSuccess(memberService.checkEmail(request));
	}
	@Deprecated
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
	@Deprecated
	@PostMapping("/reset")
	@ApiOperation(value = "user reset password", notes = "user reset password")
	public Response resetPwd(@RequestBody ForgotVO vo) {
			return Response.buildSuccess(memberService.resetPwd(vo));
	}
	@Deprecated
	@PostMapping("/changePassword")
	@ApiOperation(value = "user register", notes = "user register")
	public Response changePassword(@RequestBody ChangePasswordRequest vo) {
			return Response.buildSuccess(memberService.changePassword(vo));
	}
	
	@Deprecated
	@PostMapping("/bind")
	@ApiOperation(value = "user register", notes = "user register")
	@Loggable
	public Response bindEmailOrPhone(@RequestBody BindRequest vo) {
			return Response.buildSuccess(memberService.bind(vo));
	}
	
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{memberId}/posts")
	@ApiOperation(value = "查询某一个人的动态列表", notes = "查询某一个人的动态列表")
	@Loggable
	public Response getPostsByMemberId(@PathVariable("memberId") Long memberId,HttpServletRequest request) {
		QueryBuilder query = QueryBuilder.create(com.cloudok.bbs.mapping.PostMapping.class).with(request);
		query.and(com.cloudok.bbs.mapping.PostMapping.CREATEBY,  memberId);
		return Response.buildSuccess(postService.page(query));
	}
	
	@Deprecated
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/collect/posts")
	@ApiOperation(value = "我收藏的动态", notes = "我收藏的动态")
	@Loggable
	public Response getMyCollects(@RequestParam(name = "pageNo",defaultValue="1") Integer pageNo,
			@RequestParam(name = "pageSize",defaultValue="10") Integer pageSize) {
		return Response.buildSuccess(collectService.getMyCollectPosts(SecurityContextHelper.getCurrentUserId(),pageNo,pageSize));
	}
 
//	@PreAuthorize("isAuthenticated()")
//	@GetMapping("/link")
//	@ApiOperation(value="查询关联用户详细信息",notes="查询关联用户详细信息")
//	@Loggable
//	@Deprecated
//	public Response link(HttpServletRequest request) {
//		return Response.buildSuccess(memberService.link(QueryBuilder.create(MemberMapping.class).with(request)));
//	}
//	
//	@PreAuthorize("isAuthenticated()")
//	@GetMapping("/link/{id}")
//	@ApiOperation(value="查询关联用户详细信息",notes="查询关联用户详细信息")
//	@Loggable
//	@Deprecated
//	public Response linkById(@PathVariable("id")Long id) {
//		return Response.buildSuccess(memberService.link(id));
//	}
	
	@GetMapping("/simpleInfo")
	@ApiOperation(value="查询登录用户的简要信息",notes="查询登录用户的简要信息")
	@Loggable
	public Response simpleInfo() {
		if(!SecurityContextHelper.isLogin()) {
			return Response.buildSuccess(SimpleMemberDTO.builder().build());
		}
		return Response.buildSuccess(memberService.getSimpleMemberInfo());
	}
	
//	@PreAuthorize("isAuthenticated()")
//	@GetMapping("/identical/{id}")
//	@ApiOperation(value="获取共同的好友和标签",notes="获取共同的好友和标签")
//	@Loggable
//	public Response identical(@PathVariable("id")Long id) {
//		return Response.buildSuccess(memberService.identical(id));
//	}
//	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{type}/friend")
	@ApiOperation(value = "查询好友列表", notes = "查询好友列表 0 互关 1 我关注 2 关注我 3 新关注")
	@Loggable
	public Response friend(@PathVariable("type") Integer type,
			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
		return Response.buildSuccess(memberService.friend(type, pageNo,pageSize));
	}
	
	
//	@PreAuthorize("isAuthenticated()")
//	@GetMapping("/suggest")
//	@ApiOperation(value="推荐member列表",notes="推荐用户列表，threadId--点击上方按钮的时候生成一次，filterType目前支持 0 不过滤 1 专业 2 实习 3 个性 4 状态")
//	@Loggable
//	public Response suggest(
//			@RequestParam(name = "filterType", required=false) Integer filterType,
//			@RequestParam(name = "threadId", required=false) String threadId,
//			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
//			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
//		return Response.buildSuccess(memberService.suggest(filterType,threadId,pageNo,pageSize));
//	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/suggest")
	@ApiOperation(value="推荐member列表",notes="一天最多刷新5次总共15个，默认取当天最后一次推荐的n条，带refresh=true才去刷新下一组，filterType=0,1,2,3，4 0不过滤 1 按专业 2 按行业 3 按同好 4 校友")
	@Loggable
	public Response suggestV2(
			@RequestParam(name = "filterType", required=false,defaultValue="0") Integer filterType,
			@RequestParam(name = "refresh", required=false,defaultValue="false") Boolean refresh) {
		return Response.buildSuccess(memberService.suggestV2(filterType,refresh));
	}
	
	@GetMapping("/suggestV3")
	@ApiOperation(value="推荐member列表--流式拉取",notes="推荐用户列表，流式拉取换一个流就换一个threadId，threadId--点击上方按钮的时候生成一次,或者重新进入页面也生成一次，下拉刷新也可以刷新一次")
	@Loggable
	public Response suggestV3(
			@RequestParam(name = "threadId", required=false) String threadId,
			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
		return Response.buildSuccess(memberService.suggestV3(threadId,pageNo,pageSize));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/suggest/{memberId}/ignore")
	@ApiOperation(value="不可推荐的某人--新需求不要这个了",notes="不可推荐的某人--新需求不要这个了")
	@Loggable
	@Deprecated
	public Response ignoreSuggestMember(@PathVariable("memberId") Long memberId) {
		return Response.buildSuccess(memberService.ignoreSuggestMember(memberId));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{memberId}")
	@ApiOperation(value="查询关联用户详细信息",notes="查询关联用户详细信息")
	@Loggable
	public Response getMemberDetails(@PathVariable("memberId")Long memberId) {
		return Response.buildSuccess(memberService.getMemberDetails(memberId));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{memberId}/secondDegreeRecognized")
	@ApiOperation(value="我关注的人也关注了他分页",notes="我关注的人也关注了他分页")
	@Loggable
	public Response getSecondDegreeRecognized(@PathVariable("memberId")Long memberId,
			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
		return Response.buildSuccess(memberService.getSecondDegreeRecognized(memberId,pageNo,pageSize));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/circle")
	@ApiOperation(value="查询云圈member",notes="查询圈子，Type目前支持 1 研究领域 2 行业 3 社团 4 个性/状态标签, filterType=0 查已经关注的人的云圈，filterType=1 查未关注的人的云圈，总peers=两个查询的total相加")
	@Loggable
	@Deprecated
	public Response getMemberCircles(
			@RequestParam(name = "filterType", required=false, defaultValue="0") Integer filterType,
			@RequestParam(name = "type", required=true) Integer type,
			@RequestParam(name = "businessId", required=false) Long businessId,
			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
		return Response.buildSuccess(memberService.getMemberCircles(filterType,type,businessId,pageNo,pageSize));
	}
//	
//	@PreAuthorize("isAuthenticated()")
//	@GetMapping("/newCircle")
//	@ApiOperation(value="查询云圈member",notes="查询圈子，Type目前支持 0 动态标签 1 研究领域 2 行业 3 社团 4 个性 5状态标签 6 学校 7 专业")
//	@Loggable
//	public Response getMemberCirclesV2(
//			@RequestParam(name = "type", required=true) Integer type,
//			@RequestParam(name = "businessId", required=false) Long businessId,
//			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
//			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
//		return Response.buildSuccess(memberService.getMemberCirclesV2(type,businessId,pageNo,pageSize));
//	}
//	

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/search")
	@ApiOperation(value="根据昵称查询member",notes="根据昵称查询member")
	@Loggable
	public Response searchMembers(
			@RequestParam(name = "keywords", required=true) String keywords,
			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
		return Response.buildSuccess(memberService.searchMembers(keywords,pageNo,pageSize));
	}
	
	
	
}
