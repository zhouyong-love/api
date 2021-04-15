package com.cloudok.app.api.minapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.core.exception.SystemException;
import com.cloudok.core.vo.Response;
import com.cloudok.exception.CloudOKExceptionMessage;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import com.cloudok.minapp.service.IMinAppService;
import com.cloudok.minapp.vo.Code2SessionResult;
import com.cloudok.minapp.vo.CodeRequest;
import com.cloudok.minapp.vo.InfoRequestV2;
import com.cloudok.minapp.vo.LoginWithPhoneResult;
import com.cloudok.minapp.vo.PhoneRequest;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.vo.MemberVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("minapp-user-api")
@RequestMapping("/v1/minapp/user")
@Api(tags = "小程序用户API")
@LogModule
public class UserApi {

	@Autowired
	private IMinAppService minAppService;

	/**
	 * 登陆接口
	 */
	@PostMapping("/code2session")
	@ApiOperation(value = "通过code获取openId", notes = "通过code获取openId，如果openId存在，返回登录token等信息",response=Code2SessionResult.class)
	@Loggable
	public Response code2session(@RequestBody CodeRequest codeRequest) {
		if (codeRequest == null || StringUtils.isEmpty(codeRequest.getCode())) {
			return Response.buildFail(new SystemException("code为空",CloudOKExceptionMessage.PARSE_WEIXIN_CODE_ERROR));
		}
		return Response.buildSuccess(minAppService.code2session(codeRequest.getCode()));
	}
	
	/**
	 * 自动绑定
	 */
	@PostMapping("/bind")
	@ApiOperation(value = "自动绑定当前openId", notes = "当用户使用其他手机号登录成功的时候，获取微信login的code后 自动绑定openId与当前用户",response=Code2SessionResult.class)
	@PreAuthorize("isFullyAuthenticated()")
	@Loggable
	public Response bind(@RequestBody CodeRequest codeRequest) {
		if (codeRequest == null || StringUtils.isEmpty(codeRequest.getCode())) {
			return Response.buildFail(new SystemException("code为空",CloudOKExceptionMessage.PARSE_WEIXIN_CODE_ERROR));
		}
		return Response.buildSuccess(minAppService.bind(codeRequest.getCode()));
	}
	
	/**
	 * 自动绑定
	 */
	@GetMapping("/unbind")
	@ApiOperation(value = "解除绑定", notes = "解除openId与当前用户的绑定")
	@PreAuthorize("isFullyAuthenticated()")
	@Loggable
	public Response unbind() {
		return Response.buildSuccess(minAppService.unbind(SecurityContextHelper.getCurrentUserId()));
	}
	
//	/**
//	 *  提交用户信息
//	 */
//	@PostMapping("/myinfo")
//	@ApiOperation(value = "提交微信用户信息", notes = "提交微信用户信息",response=MemberVO.class)
//	@PreAuthorize("isFullyAuthenticated()")
//	@Loggable
//	public Response submitMyInfo(@RequestBody InfoRequest infoRequest) {
//		return Response.buildSuccess(minAppService.submitMyInfo(infoRequest));
//	}
	
	/**
	 *  提交用户信息
	 */
	@PostMapping("/myinfo")
	@ApiOperation(value = "提交微信用户信息", notes = "提交微信用户信息",response=MemberVO.class)
	@PreAuthorize("isFullyAuthenticated()")
	@Loggable
	public Response submitMyInfoV2(@RequestBody InfoRequestV2 infoRequest) {
		return Response.buildSuccess(minAppService.submitMyInfoV2(infoRequest));
	}
	
	/**
	 * <pre>
	 * 获取用户绑定手机号信息
	 * </pre>
	 */
	@PostMapping("/phone")
	@ApiOperation(value = "提交微信用户手机信息", notes = "提交微信用户手机信息,如果手机号不存在，则创建用户，如果存在则自动登录匹配用户 返回token等信息",response=LoginWithPhoneResult.class)
	@Loggable
	public Response loginWithPhone(@RequestBody PhoneRequest phoneRequest) {
		return Response.buildSuccess(this.minAppService.loginWithPhone(phoneRequest));
	}
	
}
