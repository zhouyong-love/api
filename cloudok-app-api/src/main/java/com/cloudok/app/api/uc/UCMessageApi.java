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
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import com.cloudok.log.enums.LogSwitch;
import com.cloudok.security.SecurityContextHelper;
import com.cloudok.uc.service.MessageService;
import com.cloudok.uc.service.MessageThreadService;
import com.cloudok.uc.vo.MessageVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/uc/messageThread")
@Api(tags = "消息")
@LogModule
public class UCMessageApi {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageThreadService messageThreadService;
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/latest")
	@ApiOperation(value = "获取最新消息数量",notes = "获取最新消息数量")
	@Loggable
	public Response getLatestMessageCount() {
		return Response.buildSuccess(messageThreadService.getLatestMessageCount(SecurityContextHelper.getCurrentUserId()));
	}


	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping("/{threadId}/message")
	@ApiOperation(value = "发送私信或者留言",
	notes = "添加消息-type=UCMessageType 1 认可消息 2 私信 3 留言 4 留言公开回复 5 留言私密回复，threadId为空时，后端自动生成，to的id必传，anonymous=true表示匿名留言")
	@Loggable(input=LogSwitch.OFF)
	public Response sendMessage(@PathVariable("threadId") Long threadId,
			@RequestBody @Valid MessageVO vo) {
		vo.setThreadId(threadId);
		return Response.buildSuccess(messageThreadService.createByMember(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{threadId}/message/{id}")
	@ApiOperation(value = "删除消息", notes = "删除消息")
	@Loggable
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(messageService.removeByMember(id));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{threadId}")
	@ApiOperation(value = "删除消息", notes = "删除消息")
	public Response removeByThreadId(@PathVariable("threadId") Long threadId) {
		messageService.deleteByThreadId(threadId);
		return Response.buildSuccess();
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/{threadId}")
	@ApiOperation(value = "根据threadId获取聊天内容--仅私信使用", notes = "根据threadId获取聊天内容--仅私信使用")
	@Loggable
	public Response getByThreadId(
			@PathVariable("threadId") Long threadId,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		return Response.buildSuccess(messageThreadService.getMessageByThreadId(threadId,pageNo,pageSize));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/interaction")
	@ApiOperation(value = "查询member的留言列表）", notes = "查询member的留言列表-memberId就是名片详情页的那个member的id （别人的名片或者自己的名片")
	@Loggable
	public Response searchInteractionMessages(
			@RequestParam(name = "memberId",required = false) Long memberId,
			@RequestParam(name = "status",required = false) Integer status,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		if(status != null && status != 0 && status !=1 ) {
			status = 0;
		}
		return Response.buildSuccess(messageThreadService.searchInteractionMessageThreads(memberId,status,pageNo, pageSize));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/interaction/{type}")
	@ApiOperation(value = "查发送给我的和回复给我的留言", notes = "查发送给我的和回复给我的留言 viewType=1 我收到的 viewType=2,read=1 表示自动读 回复我的")
	@Loggable
	public Response searchMyInteractionMessageThreads(
			@PathVariable(name = "type") Integer viewType,
			@RequestParam(name = "read",required = false) Integer read,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		if(viewType != null && viewType != 1 && viewType !=2 ) {
			viewType = 1;
		}
		return Response.buildSuccess(messageThreadService.searchMyInteractionMessageThreads(read,viewType,pageNo, pageSize));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/interaction/{type}/group")
	@ApiOperation(value = "查发送给我的和回复给我的留言统计", notes = "发送给我的和回复给我的留言 viewType=1 我收到的 viewType=2 回复我的")
	@Loggable
	public Response searchMyInteractionMessageThreadsGroup(@PathVariable(name = "type") Integer viewType) {
		if(viewType != null && viewType != 1 && viewType !=2 ) {
			viewType = 1;
		}
		return Response.buildSuccess(messageThreadService.searchMyInteractionMessageThreadsGroup(viewType));
	}

	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/chats")
	@ApiOperation(value = "查询私信列表", notes = "查询私信列表")
	@Loggable
	public Response searchChatMessageThreads(
			@RequestParam(name = "read",required = false) Integer read,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		return Response.buildSuccess(messageThreadService.searchChatMessageThreads(SecurityContextHelper.getCurrentUserId(), read, pageNo, pageSize));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/chats/{memberId}")
	@ApiOperation(value = "查询与某一个人的私信", notes = "查询与某一个人的私信--点击头像等进入与某一个人的私信聊天，要先获取与这个人的私信thread与最新的n条信息")
	@Loggable
	public Response getMessageThreadByMemberId(
			@RequestParam(name = "read",required = false) Integer read,
			@PathVariable(name = "memberId",required = false) Long memberId,
			@RequestParam(name = "latestMessageCount", defaultValue = "10") Integer latestMessageCount) {
		return Response.buildSuccess(messageThreadService.getMessageThreadByMemberId(SecurityContextHelper.getCurrentUserId(),read, memberId, latestMessageCount));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/chats/{id}/readed")
	@ApiOperation(value = "消息已读", notes = "消息已读")
	@Loggable
	public Response readed(
			@PathVariable(name = "id",required = false) Long messageId) {
		messageThreadService.readed(messageId);
		return Response.buildSuccess();
	}

}
