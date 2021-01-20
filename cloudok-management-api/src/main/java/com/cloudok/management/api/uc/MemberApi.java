package com.cloudok.management.api.uc;

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
import com.cloudok.uc.mapping.MemberMapping;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.vo.MemberVO;

@RestController
@RequestMapping("/v1/management/uc/member")
@Api(tags = "会员管理")
public class MemberApi {

	@Autowired
	private MemberService memberService;

	@PreAuthorize("hasAuthority('interface.member.write')")
	@PostMapping
	@ApiOperation(value = "添加会员", notes = "添加会员")
	public Response create(@RequestBody @Valid MemberVO vo) {
		return Response.buildSuccess(memberService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.member.write') or hasAuthority('interface.member.read')")
	@GetMapping
	@ApiOperation(value = "查询会员列表", notes = "查询会员列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(memberService.page(QueryBuilder.create(MemberMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.member.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改会员", notes = "修改会员")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid MemberVO vo) {
		vo.setId(id);
		return Response.buildSuccess(memberService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.member.write') or hasAuthority('interface.member.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询会员", notes = "查询会员")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(memberService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.member.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除会员", notes = "删除会员")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(memberService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.member.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除会员", notes = "批量删除会员")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(memberService.remove(ids));
	}
}
