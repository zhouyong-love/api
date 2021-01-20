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
import com.cloudok.bbs.mapping.TopicMapping;
import com.cloudok.bbs.service.TopicService;
import com.cloudok.bbs.vo.TopicVO;

@RestController
@RequestMapping("/v1/management/bbs/topic")
@Api(tags = "话题管理")
public class TopicApi {

	@Autowired
	private TopicService topicService;

	@PreAuthorize("hasAuthority('interface.topic.write')")
	@PostMapping
	@ApiOperation(value = "添加话题", notes = "添加话题")
	public Response create(@RequestBody @Valid TopicVO vo) {
		return Response.buildSuccess(topicService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.topic.write') or hasAuthority('interface.topic.read')")
	@GetMapping
	@ApiOperation(value = "查询话题列表", notes = "查询话题列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(topicService.page(QueryBuilder.create(TopicMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.topic.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改话题", notes = "修改话题")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid TopicVO vo) {
		vo.setId(id);
		return Response.buildSuccess(topicService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.topic.write') or hasAuthority('interface.topic.read')")
	@GetMapping("/{id}")
	@ApiOperation(value = "查询话题", notes = "查询话题")
	public Response modify(@PathVariable("id") Long id) {
		return Response.buildSuccess(topicService.get(id));
	}

	@PreAuthorize("hasAuthority('interface.topic.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除话题", notes = "删除话题")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(topicService.remove(id));
	}

	@PreAuthorize("hasAuthority('interface.topic.write')")
	@DeleteMapping
	@ApiOperation(value = "批量删除话题", notes = "批量删除话题")
	public Response removeList(List<Long> ids) {
		return Response.buildSuccess(topicService.remove(ids));
	}
}
