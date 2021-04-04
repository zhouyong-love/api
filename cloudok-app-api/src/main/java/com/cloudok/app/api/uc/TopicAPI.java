package com.cloudok.app.api.uc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.bbs.service.PostService;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import com.cloudok.uc.service.MemberService;
import com.cloudok.uc.service.MemberTopicService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppTopicApi")
@RequestMapping("/v1/uc/topic")
@Api(tags = "云圈API")
@LogModule
public class TopicAPI {
 
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private MemberTopicService memberTopicService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member")
	@ApiOperation(value="查询云圈member",notes="查询圈子，Type目前支持 0 动态标签 1 研究领域 2 行业 3 社团 4 个性 5状态标签 6 学校 7 专业")
	@Loggable
	public Response getMemberCirclesV2(
			@RequestParam(name = "topicType", required=true) Integer topicType,
			@RequestParam(name = "topicId", required=true) Long topicId,
			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
		return Response.buildSuccess(memberService.getMemberCirclesV2(topicType,topicId,pageNo,pageSize));
	} 
	

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/post")
	@ApiOperation(value="查询云圈动态",notes="查询动态，Type目前支持 0 动态标签 1 研究领域 2 行业 3 社团 4 个性 5状态标签 6 学校 7 专业")
	@Loggable
	public Response getPostCirclesV2(
			@RequestParam(name = "topicType", required=true) Integer topicType,
			@RequestParam(name = "topicId", required=true) Long topicId,
			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
		return Response.buildSuccess(postService.getPostCircles(topicId, topicType, pageNo, pageSize));
	} 
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/details")
	@ApiOperation(value="查询云圈基本信息",notes="查询云圈基本信息，返回云圈的 动态数，peer数以及云圈的基本信息")
	@Loggable
	public Response getTopicDetails(
			@RequestParam(name = "topicType", required=true) Integer topicType,
			@RequestParam(name = "topicId", required=true) Long topicId) {
		return Response.buildSuccess(memberTopicService.getDetails(topicType,topicId));
	} 
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/discover")
	@ApiOperation(value="云圈--发现",notes="云圈--发现")
	@Loggable
	public Response discover(
			@RequestParam(name = "pageNo", defaultValue = "1",required=false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10",required=false) Integer pageSize) {
		return Response.buildSuccess(memberTopicService.discover(pageNo,pageSize));
	} 
	
	@GetMapping
	@ApiOperation(value="人脉--云圈推荐标签",notes="人脉--云圈推荐标签")
	@Loggable
	public Response getSuggestTopics() {
		return Response.buildSuccess(memberTopicService.getSuggestTopics());
	} 
}
