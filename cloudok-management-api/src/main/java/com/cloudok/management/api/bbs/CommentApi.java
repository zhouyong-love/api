package com.cloudok.management.api.bbs;

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
import com.cloudok.bbs.mapping.CommentMapping;
import com.cloudok.bbs.service.CommentService;
import com.cloudok.bbs.vo.CommentVO;

@RestController
@RequestMapping("/v1/management/bbs/comment")
@Api(tags = "文章评论管理")
public class CommentApi {

	@Autowired
	private CommentService commentService;

	@PreAuthorize("hasAuthority('interface.comment.write')")
	@PostMapping
	@ApiOperation(value = "添加文章评论", notes = "添加文章评论")
	public Response create(@RequestBody @Valid CommentVO vo) {
		return Response.buildSuccess(commentService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.comment.write') or hasAuthority('interface.comment.read')")
	@GetMapping
	@ApiOperation(value = "查询文章评论列表", notes = "查询文章评论列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(commentService.page(QueryBuilder.create(CommentMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.comment.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改文章评论", notes = "修改文章评论")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid CommentVO vo) {
		vo.setId(id);
		return Response.buildSuccess(commentService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.comment.write') or hasAuthority('interface.comment.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询文章评论", notes = "查询文章评论")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(commentService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.comment.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除文章评论", notes = "删除文章评论")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(commentService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.comment.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除文章评论", notes = "批量删除文章评论")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(commentService.remove(ids));
	}
}
