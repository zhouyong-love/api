package com.cloudok.base.attach.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.security.token.JWTUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * 本地存储实现
 * 
 * @author xiazhijian
 *
 */
@Component("localIoHandle") //local 存储类型 必须以IoHandle结尾
public class LocalFileIoAdapter implements AttachIoHandle {
	
	private static final String STORAGETYPE = "local";
	
	@ConfigurationProperties(prefix = "attach.local")
	@Getter @Setter
	public static class LocalAttachPropterties{

		private String baseDir;
		
		private String address;
		
		private int signatureExpires;
	}
	
	@Configuration
	@EnableConfigurationProperties(LocalAttachPropterties.class)
	public static class LocalAttachConfigure{
		
	}

	@Autowired
	private LocalAttachPropterties localAttachPropterties;

	@Override
	public String write(InputStream ins, String business, String fileType, String fileName) throws IOException {
		String path = checkDirAndMake(business, fileType) + File.separator + UUID.randomUUID().toString();
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(localAttachPropterties.getBaseDir()+path);
			byte[] temp = new byte[1024];
			int length;
			while((length=ins.read(temp))>0) {
				out.write(temp,0,length);
			}
			out.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			ins.close();
			if (out != null) {
				out.close();
			}
		}
		return path;
	}

	@Override
	public String write(byte[] bs, String business, String fileType, String fileName) throws IOException {
		String path = checkDirAndMake(business, fileType) + File.separator + UUID.randomUUID().toString();
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(localAttachPropterties.getBaseDir()+path);
			out.write(bs);
			out.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return path;
	}

	@Override
	public InputStream read(AttachVO domain) throws IOException {
		File file=new File(localAttachPropterties.getBaseDir()+domain.getAddress());
		if(file.isFile()) {
			return new FileInputStream(file);
		}
		throw new FileNotFoundException("file"+ domain.getAddress() +" non-existent.");
	}

	private String checkDirAndMake(String business, String fileType) throws IOException {
		if (StringUtils.isEmpty(localAttachPropterties.getBaseDir())) {
			throw new IOException("attach.localFile.baseDir No configuration.");
		}
		if (new File(localAttachPropterties.getBaseDir()).isDirectory()) {
			Calendar cal = Calendar.getInstance();
			String path = File.separator + business + File.separator + fileType + File.separator
					+ cal.get(Calendar.YEAR) + File.separator+( cal.get(Calendar.MONTH)+ 1) + File.separator
					+ cal.get(Calendar.DATE);
			File f = new File(localAttachPropterties.getBaseDir() + path);
			if (!f.isDirectory()) {
				f.mkdirs();
			}
			return path;
		}
		throw new IOException("baseDir: " + localAttachPropterties.getBaseDir() + " non-existent.");
	}

	
	@Override
	public String sign(AttachVO vo,Map<String,String> extend) {
		return new StringBuffer(localAttachPropterties.getAddress()).append("/").append(vo.getId()).append("?sign=").append(JWTUtil.getProvisionalPass(localAttachPropterties.getSignatureExpires())).toString();
	}

	@Override
	public String getStorageType() {
		return STORAGETYPE;
	}

	@Override
	public String url(String path) {
		return localAttachPropterties.getAddress()+"/"+path;
	}
}
