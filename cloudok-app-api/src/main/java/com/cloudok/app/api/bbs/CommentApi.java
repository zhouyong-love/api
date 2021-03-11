package com.cloudok.app.api.bbs;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.bbs.service.CommentService;
import com.cloudok.bbs.vo.CommentVO;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppCommentApi")
@RequestMapping("/v1/bbs/post/{postId}/comment")
@Api(tags = "文章评论")
@LogModule
public class CommentApi {

	@Autowired
	private CommentService commentService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加文章评论", notes = "添加文章评论")
	@Loggable
	public Response create(@PathVariable("postId") Long postId, @RequestBody @Valid CommentVO vo) {
		vo.setPostId(postId);
		return Response.buildSuccess(commentService.create(vo));
	}
 

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改文章评论", notes = "修改文章评论")
	@Loggable
	public Response modify(@PathVariable("postId") Long postId, @PathVariable("id") Long id,@RequestBody @Valid CommentVO vo) {
		vo.setId(id);
		vo.setPostId(postId);
		return Response.buildSuccess(commentService.update(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@Loggable
	@ApiOperation(value = "删除文章评论", notes = "删除文章评论")
	public Response remove(@PathVariable("postId") Long postId, @PathVariable("id") Long id) {
		return Response.buildSuccess(commentService.remove(id));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "发现", notes = "发现")
	@Loggable
	public Response getCommentList(
			@PathVariable("postId") Long postId,
			@RequestParam(name = "pageNo",defaultValue="1") Integer pageNo,
			@RequestParam(name = "pageSize",defaultValue="10") Integer pageSize) {
		return Response.buildSuccess(commentService.getCommentList(postId,pageNo,pageSize));
	}
	
}
