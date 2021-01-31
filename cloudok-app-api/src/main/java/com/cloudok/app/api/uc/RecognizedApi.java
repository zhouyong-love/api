package com.cloudok.app.api.uc;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;
import com.cloudok.uc.mapping.RecognizedMapping;
import com.cloudok.uc.service.RecognizedService;
import com.cloudok.uc.vo.RecognizedVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("AppRecognizedApi")
@RequestMapping("/v1/uc/recognized")
@Api(tags = "认可某人")
public class RecognizedApi {

	@Autowired
	private RecognizedService recognizedService;

	@PreAuthorize("isFullyAuthenticated()")
	@PostMapping
	@ApiOperation(value = "添加认可某人", notes = "添加认可某人")
	public Response create(@RequestBody @Valid RecognizedVO vo) {
		return Response.buildSuccess(recognizedService.create(vo));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping
	@ApiOperation(value = "查询认可某人列表", notes = "查询认可某人列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(recognizedService.page(QueryBuilder.create(RecognizedMapping.class).with(request)));
	}

	@PreAuthorize("isFullyAuthenticated()")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除认可某人", notes = "删除认可某人")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(recognizedService.remove(id));
	}
 
}
