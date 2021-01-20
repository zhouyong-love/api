package com.cloudok.app.api.uc;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.core.vo.Response;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.vo.MemberVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppMemberApi")
@RequestMapping("/v1/uc/member")
@Api(tags = "会员表")
public class MemberApi {

	@Autowired
	private MemberService memberService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping("/register")
	@ApiOperation(value = "注册", notes = "添加会员表")
	public Response create(@RequestBody @Valid MemberVO vo) {
		return Response.buildSuccess(memberService.create(vo));
	}
 
}
