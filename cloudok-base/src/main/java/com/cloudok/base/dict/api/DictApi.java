package com.cloudok.base.dict.api;

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

import com.cloudok.base.dict.service.DictDataService;
import com.cloudok.base.dict.service.DictService;
import com.cloudok.base.dict.vo.DictDataVO;
import com.cloudok.base.dict.vo.DictVO;
import com.cloudok.core.vo.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/base/dict")
@Api(tags = "dict table")
public class DictApi {

	@Autowired
	private DictService dictService;
	
	@Autowired
	private DictDataService dictDataService;
	
	@PreAuthorize("hasAuthority('interface.dict.write')")
	@PostMapping
	@ApiOperation(value = "添加dict table", notes = "添加dict table")
	public Response create(@RequestBody @Valid DictVO vo) {
		return Response.buildSuccess(dictService.create(vo));
	}
	
//	@PreAuthorize("hasAuthority('interface.dict.write')")
	@PostMapping("/reflashCache")
	@ApiOperation(value = "刷新缓存", notes = "刷新缓存")
	public Response reflashCache() {
		dictService.reflashCache();
		return Response.buildSuccess();
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{dictCode}/values")
	@ApiOperation(value = "拉取字典值列表(从缓存)", notes = "拉取字典值列表(从缓存)")
	public Response values(@PathVariable("dictCode")String dictCode) {
		return Response.buildSuccess(dictService.findAllFromCache(dictCode));
	}

	@PreAuthorize("hasAuthority('interface.dict.write') or hasAuthority('interface.dict.read')")
	@GetMapping
	@ApiOperation(value = "获取字典类型列表（含枚举，不分页）", notes = "获取字典类型列表（含枚举，不分页）")
	public Response search(@RequestParam("dictCode")String dictCode,@RequestParam("dictName")String dictName,@RequestParam("remark")String remark) {
		return Response.buildSuccess(dictService.findAll(dictCode, dictName, remark));
	}

	@PreAuthorize("hasAuthority('interface.dict.write')")
	@PutMapping("/{id}")
	@ApiOperation(value = "修改dict table", notes = "修改dict table")
	public Response modify(@PathVariable("id") Long id,@RequestBody @Valid DictVO vo) {
		vo.setId(id);
		return Response.buildSuccess(dictService.update(vo));
	}

	@PreAuthorize("hasAuthority('interface.dict.write')")
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除dict table", notes = "删除dict table")
	public Response remove(@PathVariable("id") Long id) {
		return Response.buildSuccess(dictService.remove(id));
	}

	
	@PreAuthorize("hasAuthority('interface.dict.write')")
	@PostMapping("/{dictCode}")
	@ApiOperation(value = "添加dict data table", notes = "添加dict value table")
	public Response createDetail(@RequestBody @Valid DictDataVO vo,@PathVariable("dictCode") String dictCode) {
		vo.setDictCode(dictCode);
		return Response.buildSuccess(dictDataService.create(vo));
	}

	@PreAuthorize("hasAuthority('interface.dict.write') or hasAuthority('interface.dict.read')")
	@GetMapping("/{dictCode}")
	@ApiOperation(value = "获取字典值列表", notes = "获取字典值列表")
	public Response searchData(@PathVariable("dictCode")String dictCode) {
		return Response.buildSuccess(dictDataService.findAll(dictCode));
	}

	@PreAuthorize("hasAuthority('interface.dict.write')")
	@PutMapping("/{dictCode}/{id}")
	@ApiOperation(value = "修改dict data table", notes = "修改dict data table")
	public Response modifyData(@PathVariable("id") Long id,@RequestBody @Valid DictDataVO vo,@PathVariable("dictCode")String dictCode) {
		vo.setId(id);
		vo.setDictCode(dictCode);
		return Response.buildSuccess(dictDataService.update(vo));
	}


	@PreAuthorize("hasAuthority('interface.dict.write')")
	@DeleteMapping("/{dictCode}/{id}")
	@ApiOperation(value = "删除dict data table", notes = "删除dict data table")
	public Response removeData(@PathVariable("id") Long id) {
		return Response.buildSuccess(dictDataService.remove(id));
	}

}
