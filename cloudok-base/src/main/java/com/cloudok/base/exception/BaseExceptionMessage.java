package com.cloudok.base.exception;

import com.cloudok.core.exception.ExceptionMessage;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月29日 下午5:10:02
 */
public class BaseExceptionMessage extends ExceptionMessage{

	/**
	 * @param code
	 * @param message
	 */
	public BaseExceptionMessage(String code, String message) {
		super(code, message);
	}

	private static final long serialVersionUID = -3367320037968145163L;

	public static final BaseExceptionMessage ATTACH_UPLOAD_SUFFIX_ERROR=new BaseExceptionMessage("ATTACH.UPLOAD_SUFFIX_ERROR", "File suffix not supported.");
	
	public static final BaseExceptionMessage ATTACH_UPLOAD_MAXIMUM_ERROR=new BaseExceptionMessage("ATTACH.UPLOAD_MAXIMUM_ERROR", "Attachment size exceeds the maximum allowed limit.");

	public static final BaseExceptionMessage ATTACH_UPLOAD_ERROR=new BaseExceptionMessage("ATTACH.UPLOAD_ERROR", "Attachment upload failed.");
	
	public static final BaseExceptionMessage ATTACH_DOWNLOAD_ERROR=new BaseExceptionMessage("ATTACH.DOWNLOAD_ERROR", "Attachment download failed.");

}
