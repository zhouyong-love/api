package com.cloudok.app.api.uc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import com.cloudok.uc.service.NotificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppNotificationApi")
@RequestMapping("/v1/uc/notification")
@Api(tags = "消息提醒")
@LogModule
public class NotificationAPI {
 
	@Autowired
	private NotificationService notificationService;
	 
	

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/post/total")
	@ApiOperation(value="动态消息提醒--查看消息提醒总数",notes="查看消息提醒总数，type=1 评论  type=2 点赞")
	@Loggable
	public Response getTotal() {
		return Response.buildSuccess(notificationService.getTotal());
	} 
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/post")
	@ApiOperation(value="动态消息提醒--展开详情",notes="展开详情--type=1 评论  type=2 点赞")
	@Loggable
	public Response getNotificationList(
			@RequestParam(name = "type", required=true) Integer type,
			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
		return Response.buildSuccess(notificationService.getNotificationList(type,pageNo,pageSize));
	}  
}
