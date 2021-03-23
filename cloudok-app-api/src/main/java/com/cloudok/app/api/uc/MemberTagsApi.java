package com.cloudok.app.api.uc;

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

import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.service.MemberTagsService;
import com.cloudok.uc.vo.MemberTagsVO;
import com.cloudok.uc.vo.SwitchSNRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppMemberTagsApi")
@RequestMapping("/v1/uc/tags")
@Api(tags = "用户标签")
@LogModule
public class MemberTagsApi {

	@Autowired
	private MemberTagsService memberTagsService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加用户标签", notes = "添加用户标签")
	@Loggable
	public Response create(@RequestBody @Valid MemberTagsVO vo) {
		return Response.buildSuccess(memberTagsService.createByMember(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询用户标签列表", notes = "查询用户标签列表")
	@Loggable
	public Response search() {
		return Response.buildSuccess(memberTagsService.getByMember(SecurityContextHelper.getCurrentUserId()));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询用户标签列表", notes = "查询用户标签列表")
	@Loggable
	public Response get(@PathVariable("id")Long id) {
		return Response.buildSuccess(memberTagsService.getByMember(SecurityContextHelper.getCurrentUserId(),id));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改用户标签", notes = "修改用户标签")
	@Loggable
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid MemberTagsVO vo) {
		vo.setId(id);
		return Response.buildSuccess(memberTagsService.updateByMember(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除用户标签", notes = "删除用户标签")
	@Loggable
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(memberTagsService.remove(id));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/switchSN")
	@Loggable
	@ApiOperation(value = "调整排序", notes = "调整排序")
	public Response switchSN(@RequestBody @Valid SwitchSNRequest switchSNRequest) {
		return Response.buildSuccess(memberTagsService.switchSN(switchSNRequest));
	}
}
