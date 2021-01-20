package com.cloudok.app.api.bbs;

import javax.servlet.http.HttpServletRequest;
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

import com.cloudok.bbs.service.PostService;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppPostApi")
@RequestMapping("/v1/bbs/post")
@Api(tags = "动态、帖子等")
public class PostApi {

	@Autowired
	private PostService postService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加动态、帖子等", notes = "添加动态、帖子等")
	public Response create(@RequestBody @Valid PostVO vo) {
		return Response.buildSuccess(postService.create(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询动态、帖子等列表", notes = "查询动态、帖子等列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(postService.page(QueryBuilder.create(com.cloudok.bbs.mapping.PostMapping.class).with(request)));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改动态、帖子等", notes = "修改动态、帖子等")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid PostVO vo) {
		vo.setId(id);
		return Response.buildSuccess(postService.update(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询动态、帖子等", notes = "查询动态、帖子等")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(postService.get(id));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除动态、帖子等", notes = "删除动态、帖子等")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(postService.remove(id));
	}
 
}
