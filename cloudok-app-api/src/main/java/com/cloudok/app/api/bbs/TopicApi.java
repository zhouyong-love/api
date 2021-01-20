package com.cloudok.app.api.bbs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.bbs.mapping.TopicMapping;
import com.cloudok.bbs.service.TopicService;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppTopicApi")
@RequestMapping("/v1/bbs/topic")
@Api(tags = "话题")
public class TopicApi {

	@Autowired
	private TopicService topicService;

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询话题列表", notes = "查询话题列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(topicService.page(QueryBuilder.create(TopicMapping.class).with(request)));
	} 
}
