package com.cloudok.app.api.bbs;

import java.util.Arrays;
import java.util.stream.Collectors;

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

import com.cloudok.bbs.service.PostService;
import com.cloudok.bbs.vo.PostVO;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppPostApi")
@RequestMapping("/v1/bbs/post")
@Api(tags = "动态、帖子等")
@LogModule
public class PostApi {

	@Autowired
	private PostService postService;
	
	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加动态、帖子等", notes = "添加动态、帖子等")
	@Loggable
	public Response create(@RequestBody @Valid PostVO vo) {
		return Response.buildSuccess(postService.create(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改动态、帖子等", notes = "修改动态、帖子等")
	@Loggable
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid PostVO vo) {
		vo.setId(id);
		return Response.buildSuccess(postService.update(vo));
	}


	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping("/{id}/collect")
	@ApiOperation(value = "收藏动态", notes = "收藏动态")
	@Loggable
	public Response collect(@PathVariable("id") Long id) {
		return Response.buildSuccess(postService.collect(id));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping("/{id}/cancelCollect")
	@ApiOperation(value = "取消收藏动态", notes = "取消收藏动态")
	@Loggable
	public Response cancelCollect(@PathVariable("id") Long id) {
		return Response.buildSuccess(postService.cancelCollect(id));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping("/{id}/thumbsUp")
	@ApiOperation(value = "点赞", notes = "点赞")
	@Loggable
	public Response thumbsUp(@PathVariable("id") Long id) {
		return Response.buildSuccess(postService.thumbsUp(id));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping("/{id}/cancelThumbsUp")
	@ApiOperation(value = "取消点赞", notes = "取消点赞")
	@Loggable
	public Response cancelThumbsUp(@PathVariable("id") Long id) {
		return Response.buildSuccess(postService.cancelThumbsUp(id));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询动态、帖子等", notes = "查询动态、帖子等")
	@Loggable
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(postService.get(id));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除动态、帖子等", notes = "删除动态、帖子等")
	@Loggable
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(postService.remove(id));
	}
	

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/discover")
	@ApiOperation(value = "发现", notes = "发现")
	@Loggable
	public Response searchByTopic(@RequestParam("topicIdList") String topicIdList,
			@RequestParam(name = "pageNo",defaultValue="1") Integer pageNo,
			@RequestParam(name = "pageSize",defaultValue="10") Integer pageSize) {
		return Response.buildSuccess(postService.searchByTopic(
				Arrays.asList(topicIdList.split(",")).stream().map(item -> Long.parseLong(item)).collect(Collectors.toList()),
				pageNo,
				pageSize));
	}
 
}
