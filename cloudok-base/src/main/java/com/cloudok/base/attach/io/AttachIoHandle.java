package com.cloudok.base.attach.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.cloudok.base.attach.vo.AttachVO;


/**
 * 附件读写接口
 * 
 * @author xiazhijian
 *
 */
public interface AttachIoHandle {
	
	
	/**
	 * 下载签名
	 * @param attachVO
	 * @param extend
	 * @return
	 */
	public String sign(AttachVO attachVO,Map<String,String> extend);
	
	/**
	 * 写入存储
	 * @param ins
	 * @param business
	 * @param fileType
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public String write(InputStream ins,String business,String fileType,String fileName) throws IOException;

	/**
	 * 写入存储
	 * @param bs
	 * @param business
	 * @param fileType
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public String write(byte[] bs,String business,String fileType,String fileName) throws IOException;

	/**
	 * 读取存储
	 * @param vo
	 * @return
	 * @throws IOException
	 */
	public InputStream read(AttachVO vo) throws IOException;
	
	public String getStorageType();
	
	public String url(String path);

}
