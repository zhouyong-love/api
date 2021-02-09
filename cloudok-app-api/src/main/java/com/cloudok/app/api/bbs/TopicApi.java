package com.cloudok.app.api.bbs;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.bbs.mapping.TopicMapping;
import com.cloudok.bbs.service.PostService;
import com.cloudok.bbs.service.TopicService;
import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppTopicApi")
@RequestMapping("/v1/bbs/topic")
@Api(tags = "话题")
@LogModule
public class TopicApi {

	@Autowired
	private TopicService topicService;
	
	@Autowired
	private PostService postService;

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询话题列表", notes = "查询话题列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(topicService.page(QueryBuilder.create(TopicMapping.class).with(request).sort(Mapping.ID).asc()));
	} 
	

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{topicId}/bbs")
	@ApiOperation(value = "指定话题下的动态", notes = "指定话题下的动态")
	@Loggable
	public Response searchByTopic(@PathVariable("topicId") Long topicId,@RequestParam(name = "pageNo",defaultValue="0") Integer pageNo,
			@RequestParam(name = "pageSize",defaultValue="10") Integer pageSize) {
		return Response.buildSuccess(postService.searchByTopic(Collections.singletonList(topicId),pageNo,pageSize));
	}
}
