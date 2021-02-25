package com.cloudok.base.attach.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudok.base.attach.AttachAuthorityHandle;
import com.cloudok.base.attach.AttachConfigure;
import com.cloudok.base.attach.AttachProperties.AttachBusiness;
import com.cloudok.base.attach.AttachProperties.AttachBusiness.FileType;
import com.cloudok.base.attach.mapping.AttachMapping;
import com.cloudok.base.attach.service.AttachService;
import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.base.exception.BaseExceptionMessage;
import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.SystemException;
import com.cloudok.core.query.QueryBuilder;
import com.cloudok.core.query.QueryOperator;
import com.cloudok.security.exception.SecurityExceptionMessage;
import com.cloudok.util.FileUtil;


public class AttachRWHandle {

	public static AttachVO sign(Long id) {
		AttachVO attachVO = SpringApplicationContext.getBean(AttachService.class).get(id);
		attachVO.setUrl(SpringApplicationContext.getBean(attachVO.getStorageModel()+"IoHandle", AttachIoHandle.class).sign(attachVO,null));
		return attachVO;
	}
	
	public static AttachVO sign(AttachVO attachVO) {
		return sign(attachVO, null);
	}
	public static AttachVO sign(AttachVO attachVO,Map<String,String> extend) {
		attachVO.setUrl(SpringApplicationContext.getBean(attachVO.getStorageModel()+"IoHandle", AttachIoHandle.class).sign(attachVO,extend));
		return attachVO;
	}

	/**
	 * 批量签名
	 * 
	 * @param ids
	 * @return
	 */
	public static Map<Long, AttachVO> sign(Map<String,String> extend,Long... ids) {
		List<AttachVO> list = SpringApplicationContext.getBean(AttachService.class)
				.list(QueryBuilder.create(AttachMapping.class).and(AttachMapping.ID, QueryOperator.IN, ids).end());
		Map<Long, AttachVO> map = new HashMap<Long, AttachVO>();
		for (AttachVO attachVO : list) {
			map.put(attachVO.getId(), sign(attachVO,extend));
		}
		return map;
	}

	/**
	 * 按业务id签名
	 * 
	 * @param i
	 * @return
	 */
	public static List<AttachVO> signBybusiness(Long id) {
		List<AttachVO> list = SpringApplicationContext.getBean(AttachService.class)
				.list(QueryBuilder.create(AttachMapping.class).and(AttachMapping.BUSINESSID, id).end());
		for (AttachVO attachVO : list) {
			sign(attachVO);
		}
		return list;
	}

	/**
	 * 按业务id签名
	 * 
	 * @param i
	 * @return
	 */
	public static List<AttachVO> signBybusinesss(List<Long> idList) {
		List<AttachVO> list = SpringApplicationContext.getBean(AttachService.class).list(QueryBuilder
				.create(AttachMapping.class).and(AttachMapping.BUSINESSID, QueryOperator.IN, idList).end());
		for (AttachVO attachVO : list) {
			sign(attachVO);
		}
		return list;
	}

	/**
	 * 批量签名
	 * 
	 * @param ids
	 * @return
	 */
	public static List<AttachVO> sign(List<Long> idList) {
		if (CollectionUtils.isEmpty(idList)) {
			return Collections.emptyList();
		}
		List<AttachVO> list = SpringApplicationContext.getBean(AttachService.class).list(
				QueryBuilder.create(AttachMapping.class).and(AttachMapping.ID, QueryOperator.IN, idList).end());
		for (AttachVO attachVO : list) {
			sign(attachVO);
		}
		return list;
	}

	/**
	 * 表单上传
	 * 
	 * @param file
	 * @param business
	 * @param fileType
	 * @param params
	 * @throws IOException
	 */
	public static AttachVO upload(MultipartFile file, String business, String fileType, Map<String, String> params) {
		AttachBusiness config = AttachConfigure.getConfig(business);
		FileType fileTypeConfig = config.getFileTypes().get(fileType);
		String suffix = FileUtil.getSuffix(file.getOriginalFilename());
		if (fileTypeConfig.getSuffix() != null && fileTypeConfig.getSuffix().length> 0) {
			if (!Arrays.asList(fileTypeConfig.getSuffix()).contains(suffix)) {
				throw new SystemException(BaseExceptionMessage.ATTACH_UPLOAD_SUFFIX_ERROR);
			}
		}
		if (fileTypeConfig.getMaxSize() < file.getSize()) {
			throw new SystemException(BaseExceptionMessage.ATTACH_UPLOAD_MAXIMUM_ERROR);
		}
		if (config.getAuthorityInterface() != null) {
			if (!SpringApplicationContext.getBean(config.getAuthorityInterface(), AttachAuthorityHandle.class).uploader(config, params)) {
				throw new SystemException(SecurityExceptionMessage.PERMISSION_DENIED);
			}
		}
		try {
			
			String code = SpringApplicationContext.getBean(config.getStorageType()+"IoHandle", AttachIoHandle.class).write(file.getInputStream(), business, fileType,
					file.getOriginalFilename());
			AttachVO attachVO = new AttachVO();
			attachVO.setAddress(code);
			attachVO.setBusinessKey(business);
			attachVO.setFileType(fileType);
			attachVO.setStorageModel(config.getStorageType());
			attachVO.setUsed(false);
			attachVO.setFileName(file.getOriginalFilename());
			attachVO.setSuffix(suffix);
			attachVO.setFileSize(file.getSize());
			SpringApplicationContext.getBean(AttachService.class).create(attachVO);
			return attachVO;
		} catch (IOException e) {
			e.printStackTrace();
			throw new SystemException(BaseExceptionMessage.ATTACH_UPLOAD_ERROR);
		}
	}

	/**
	 * 表单base64上传
	 * 
	 * @param base64
	 * @param business
	 * @param fileType
	 * @param fileName
	 * @param params
	 * @throws IOException
	 */
	public static AttachVO upload(String base64, String business, String fileType, String fileName,
			Map<String, String> params)  {
		byte[] fileByte = Base64Utils.decodeFromString(base64);
		AttachBusiness config = AttachConfigure.getConfig(business);
		FileType fileTypeConfig = config.getFileTypes().get(fileType);
		String suffix = FileUtil.getSuffix(fileName);
		if (fileTypeConfig.getSuffix() != null && fileTypeConfig.getSuffix().length> 0) {
			if (!Arrays.asList(fileTypeConfig.getSuffix()).contains(suffix)) {
				throw new SystemException(BaseExceptionMessage.ATTACH_UPLOAD_SUFFIX_ERROR);
			}
		}
		if (fileTypeConfig.getMaxSize() < fileByte.length) {
			throw new SystemException(BaseExceptionMessage.ATTACH_UPLOAD_MAXIMUM_ERROR);
		}
		if (config.getAuthorityInterface() != null) {
			if (!SpringApplicationContext.getBean(config.getAuthorityInterface(), AttachAuthorityHandle.class).uploader(config, params)) {
				throw new SystemException(SecurityExceptionMessage.PERMISSION_DENIED);
			}
		}
		try {
			
			String code = SpringApplicationContext.getBean(config.getStorageType()+"IoHandle", AttachIoHandle.class).write(fileByte, business, fileType, fileName);
			AttachVO attachVO = new AttachVO();
			attachVO.setAddress(code);
			attachVO.setBusinessKey(business);
			attachVO.setFileType(fileType);
			attachVO.setStorageModel(config.getStorageType());
			attachVO.setUsed(false);
			attachVO.setFileName(fileName);
			attachVO.setSuffix(suffix);
			attachVO.setFileSize((long) fileByte.length);
			SpringApplicationContext.getBean(AttachService.class).create(attachVO);
			return attachVO;
		} catch (IOException e) {
			e.printStackTrace();
			throw new SystemException(BaseExceptionMessage.ATTACH_UPLOAD_ERROR);
		}
	}

	/**
	 * 文件存入附件
	 * 
	 * @param file
	 * @param business
	 * @param fileType
	 * @param fileName
	 * @param params
	 * @throws IOException
	 */
	public static AttachVO upload(File file, String business, String fileType, String fileName,
			Map<String, String> params) throws IOException {
		AttachBusiness config = AttachConfigure.getConfig(business);
		FileType fileTypeConfig = config.getFileTypes().get(fileType);
		String suffix = FileUtil.getSuffix(fileName);
		if (fileTypeConfig.getSuffix() != null && fileTypeConfig.getSuffix().length> 0) {
			if (!Arrays.asList(fileTypeConfig.getSuffix()).contains(suffix)) {
				throw new SystemException(BaseExceptionMessage.ATTACH_UPLOAD_SUFFIX_ERROR);
			}
		}
		if (fileTypeConfig.getMaxSize() < file.length()) {
			throw new SystemException(BaseExceptionMessage.ATTACH_UPLOAD_MAXIMUM_ERROR);
		}
		if (config.getAuthorityInterface() != null) {
			if (!SpringApplicationContext.getBean(config.getAuthorityInterface(), AttachAuthorityHandle.class).uploader(config, params)) {
				throw new SystemException(SecurityExceptionMessage.PERMISSION_DENIED);
			}
		}
		try {
			
			String code = SpringApplicationContext.getBean(config.getStorageType()+"IoHandle", AttachIoHandle.class).write( new FileInputStream(file), business, fileType, fileName);
			AttachVO attachVO = new AttachVO();
			attachVO.setAddress(code);
			attachVO.setBusinessKey(business);
			attachVO.setFileType(fileType);
			attachVO.setStorageModel(config.getStorageType());
			attachVO.setUsed(false);
			attachVO.setFileName(fileName);
			attachVO.setSuffix(suffix);
			attachVO.setFileSize((long) file.length());
			SpringApplicationContext.getBean(AttachService.class).create(attachVO);
			return attachVO;
		} catch (IOException e) {
			e.printStackTrace();
			throw new SystemException(BaseExceptionMessage.ATTACH_UPLOAD_ERROR);
		}
	}

	/**
	 * 下载附件
	 * 
	 * @param domain
	 * @return
	 * @throws IOException
	 */
	public static InputStream dowload(AttachVO domain, Map<String, String> params) throws IOException {
		AttachBusiness config = AttachConfigure.getConfig(domain.getBusinessKey());
		if (config.getAuthorityInterface() != null) {
			if (!SpringApplicationContext.getBean(config.getAuthorityInterface(), AttachAuthorityHandle.class).download(config, params)) {
				throw new SystemException(SecurityExceptionMessage.PERMISSION_DENIED);
			}
		}

		return SpringApplicationContext.getBean(domain.getStorageModel()+"IoHandle", AttachIoHandle.class).read(domain);
	}



	/**
	 * 更新附件状态
	 * 
	 * @param attachId
	 * @param businessId
	 * @param remark
	 */
	public static void used(Long attachId, Long businessId, String remark) {
		AttachVO attach = new AttachVO();
		attach.setId(attachId);
		attach.setUsed(true);
		attach.setBusinessId(businessId);
		attach.setBusinessRemark(remark);
		SpringApplicationContext.getBean(AttachService.class).merge(attach);
	}

	/**
	 * 更新附件状态
	 * 
	 * @param attachId
	 * @param businessId
	 * @param remark
	 */
	public static void used(List<AttachVO> list) {
		AttachService attachServer = SpringApplicationContext.getBean(AttachService.class);
		if (!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(item -> {
				AttachVO vo=new AttachVO();
				vo.setId(item.getId());
				vo.setBusinessId(item.getBusinessId());
				vo.setUsed(true);
				attachServer.merge(vo);
			});
		}
	}

	/**
	 * 删除附件
	 * 
	 * @param i
	 * @return
	 */
	public static int delete(Long id) {
		return SpringApplicationContext.getBean(AttachService.class).remove(id);
	}

	/**
	 * 删除附件
	 * 
	 * @param i
	 * @return
	 */
	public static int delete(List<Long> idList) {
		return SpringApplicationContext.getBean(AttachService.class).remove(idList);
	}
}
