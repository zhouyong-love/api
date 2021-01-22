package com.cloudok.base.attach.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudok.base.attach.AttachProperties;
import com.cloudok.base.attach.io.AttachRWHandle;
import com.cloudok.base.attach.mapping.AttachMapping;
import com.cloudok.base.attach.service.AttachService;
import com.cloudok.base.attach.util.AttachUtil;
import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.base.attach.vo.Base64FileVO;
import com.cloudok.base.exception.BaseExceptionMessage;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.vo.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/base/attach")
@Api(tags = "attach table")
public class AttachApi {

	@Autowired
	private AttachService attachService;
	
	@Autowired
	private AttachProperties attachProperties;

	@PreAuthorize("hasAuthority('interface.attach.read')")
	@GetMapping
	@ApiOperation(value = "查询附件信息列表", notes = "查询附件信息列表")
	public Response search(HttpServletRequest request) {
		return Response.buildSuccess(attachService.page(QueryBuilder.create(AttachMapping.class).with(request)));
	}

	@PreAuthorize("hasAuthority('interface.attach.read')")
	@GetMapping(path = "/config")
	@ApiOperation(value = "查询附件配置信息", notes = "查询附件配置信息")
	public Response getBusiness() {
		return Response.buildSuccess(attachProperties.getBusiness());
	}

	@GetMapping(path = "/download/{id}")
	@ApiOperation(value = "附件下载", notes = "附件下载")
	public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) {
		AttachVO attachVO = attachService.get(id);
		try {
			AttachUtil.sendFile(request, response, attachVO,
					AttachRWHandle.dowload(attachVO, AttachUtil.getParams(request)));
		} catch (IOException e) {
			throw new SystemException(BaseExceptionMessage.ATTACH_DOWNLOAD_ERROR);
		}
	}

	@GetMapping(path = "/sign/{id}")
	@ApiOperation(value = "附件下载签名", notes = "附件下载签名")
	public Response sign(@PathVariable("id") Long id) {
		return Response.buildSuccess(AttachRWHandle.sign(attachService.get(id)));
	}
	
	@PostMapping(path = "/sign")
	@ApiOperation(value = "附件下载签名", notes = "附件下载签名")
	public Response batchSign(@RequestBody List<Long> ids) {
		return Response.buildSuccess(AttachRWHandle.sign(ids));
	}

	@PostMapping(path = "/upload/{business}/{fileType}")
	@ApiOperation(value = "附件上传", notes = "附件上传")
	public Response upload(HttpServletRequest request, MultipartFile file, @PathVariable("business") String business,
			@PathVariable("fileType") String fileType) {
		AttachVO vo = AttachRWHandle.upload(file, business, fileType, AttachUtil.getParams(request));
		return Response.buildSuccess(AttachRWHandle.sign(vo));
	}

	@PostMapping(path = "/uploadBase64/{business}/{fileType}")
	@ApiOperation(value = "附件上传", notes = "附件上传")
	public Response uploadBase64(HttpServletRequest request, @RequestBody Base64FileVO file,
			@PathVariable("business") String business, @PathVariable("fileType") String fileType) {
		AttachVO vo = AttachRWHandle.upload(file.getBase64(), business, fileType, file.getFileName(),
				AttachUtil.getParams(request));
		return Response.buildSuccess(AttachRWHandle.sign(vo));
	}
}
