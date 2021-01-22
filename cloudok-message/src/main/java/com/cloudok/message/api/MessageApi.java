package com.cloudok.message.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.message.mapping.MessageDetailsMapping;
import com.cloudok.message.mapping.MessageMapping;
import com.cloudok.message.service.MessageDetailsService;
import com.cloudok.message.service.MessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/message")
@Api(tags = "消息表")
public class MessageApi {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageDetailsService messageDetailService;

	@PreAuthorize("hasAuthority('message.write')")
	@GetMapping("/messageType")
	@ApiOperation(value = "获取消息类型列表", notes = "获取消息类型列表")
	public Response create() {
		return Response.buildSuccess(messageService.messageTypes());
	}
	
	@PreAuthorize("hasAuthority('message.write') or hasAuthority('message.read')")
	@GetMapping
	@ApiOperation(value = "查询消息表列表", notes = "查询消息表列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(messageService.page(QueryBuilder.create(MessageMapping.class).with(request)));
	}
	
	@PreAuthorize("hasAuthority('message.write') or hasAuthority('message.read')")
	@GetMapping("/{id}/receive")
	@ApiOperation(value = "或消息接收者列表", notes = "或消息接收者列表")
	public Response receive(HttpServletRequest request,@PathVariable("id")Long id) {
		return Response.buildSuccess(messageDetailService.page(QueryBuilder.create(MessageDetailsMapping.class).with(request).and(MessageDetailsMapping.MESSAGEID, id).end()));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/ii")
	@ApiOperation(value = "站内信列表", notes = "站内信列表")
	public Response ii(HttpServletRequest request) {
		return Response.buildSuccess(messageService.iipage(QueryBuilder.create(MessageMapping.class).with(request)));
	}

	@PreAuthorize("isAuthenticated()")
	@PutMapping("/ii/read")
	@ApiOperation(value = "标记已读（body 为空标记所有", notes = "标记已读（body 为空标记所有")
	public Response modify(@RequestBody @Valid List<Long> ids) {
		messageService.read(ids);
		return Response.buildSuccess();
	}
	
}