package com.cloudok.app.api.uc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.log.annotation.LogModule;
import com.cloudok.log.annotation.Loggable;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.vo.RecognizedVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppRecognizedApi")
@RequestMapping("/v1/uc/recognized")
@Api(tags = "认可某人")
@LogModule
public class RecognizedApi {

	@Autowired
	private RecognizedService recognizedService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加认可某人", notes = "添加认可某人")
	@Loggable
	public Response create(@RequestBody @Valid RecognizedVO vo) {
		return Response.buildSuccess(recognizedService.recognized(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询认可某人列表", notes = "查询认可某人列表")
	@Loggable
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(recognizedService.page(QueryBuilder.create(RecognizedMapping.class).with(request)));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}/member")
	@ApiOperation(value = "删除认可某人", notes = "删除认可某人")
	@Loggable
	public Response remove(@PathVariable("id") Long memberId) {
		return Response.buildSuccess(recognizedService.unRecognized(memberId));
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@PutMapping("/read")
	@ApiOperation(value = "关注状态已读", notes = "关注状态已读")
	@Loggable
	public Response read(@RequestBody List<Long> memberIds) {
		recognizedService.read(memberIds);
		return Response.buildSuccess();
	}
}
