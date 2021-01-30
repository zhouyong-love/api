package com.cloudok.app.api.uc;

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

import com.cloudok.core.vo.Response;
import com.cloudok.uc.service.MessageService;
import com.cloudok.uc.vo.MessageVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/uc/{memberId}")
@Api(tags = "消息 memberId--目标member")
public class UCMessageApi {

	@Autowired
	private MessageService messageService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping("/thread/{threadId}/message")
	@ApiOperation(value = "添加消息-type=UCMessageType 1 认可消息 2 私信 3 匿名互动 4 实名互动, threadId为空时，后端自动生成", notes = "添加消息-type=UCMessageType 1 认可消息 2 私信 3 匿名互动 4 实名互动，threadId为空时，后端自动生成")
	public Response create(@PathVariable("memberId") Long memberId, @PathVariable("threadId") Long threadId,
			@RequestBody @Valid MessageVO vo) {
		vo.setThreadId(threadId);
		return Response.buildSuccess(messageService.createByMember(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/thread/{threadId}/message/{id}")
	@ApiOperation(value = "修改消息", notes = "修改消息")
	public Response modify(@PathVariable("memberId") Long memberId, @PathVariable("threadId") Long threadId,
			@PathVariable("id") Long id, @RequestBody @Valid MessageVO vo) {
		vo.setId(id);
		return Response.buildSuccess(messageService.updateByMember(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("thread/{threadId}/message/{id}")
	@ApiOperation(value = "删除消息", notes = "删除消息")
	public Response remove(@PathVariable("memberId") Long memberId, @PathVariable("id") Long id) {
		return Response.buildSuccess(messageService.removeByMember(id));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/thread/{threadId}")
	@ApiOperation(value = "根据threadId获取聊天内容", notes = "根据threadId获取聊天内容")
	public Response getByThreadId(@PathVariable("memberId") Long memberId, @PathVariable("id") Long id,@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		return Response.buildSuccess(messageService.getByThreadId(id,pageNo,pageSize));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/interaction")
	@ApiOperation(value = "查询member的互动消息列表", notes = "查询member的互动消息列表")
	public Response searchInteractionMessages(@PathVariable("memberId") Long memberId,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		return Response.buildSuccess(messageService.searchInteractionMessages(memberId, pageNo, pageSize));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/private")
	@ApiOperation(value = "查询member的私信消息列表", notes = "查询member的私信消息列表")
	public Response searchPrivateMessages(@PathVariable("memberId") Long memberId,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		return Response.buildSuccess(messageService.searchPrivateMessages(memberId, pageNo, pageSize));
	}

}
