package com.cloudok.app.api.uc;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.uc.mapping.MemberTagsMapping;
import com.cloudok.uc.service.MemberTagsService;
import com.cloudok.uc.vo.MemberTagsVO;

@RestController("AppMemberTagsApi")
@RequestMapping("/v1/uc/member/{memberId}/tags")
@Api(tags = "用户标签")
public class MemberTagsApi {

	@Autowired
	private MemberTagsService memberTagsService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加用户标签", notes = "添加用户标签")
	public Response create(@RequestBody @Valid MemberTagsVO vo) {
		return Response.buildSuccess(memberTagsService.create(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询用户标签列表", notes = "查询用户标签列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(memberTagsService.page(QueryBuilder.create(MemberTagsMapping.class).with(request)));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改用户标签", notes = "修改用户标签")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid MemberTagsVO vo) {
		vo.setId(id);
		return Response.buildSuccess(memberTagsService.update(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping
	@ApiOperation(value = "批量删除用户标签", notes = "批量删除用户标签")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(memberTagsService.remove(ids));
	}
}
