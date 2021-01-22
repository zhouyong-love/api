package com.cloudok.base.attach.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudok.base.attach.vo.AttachVO;
import com.cloudok.util.ContentTypeUtil;
import com.cloudok.util.DateTimeUtil;

public class AttachUtil {

	/**
	 * 参数转map
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getParams(HttpServletRequest request) {
		Enumeration<String> e = request.getParameterNames();
		Map<String, String> map = new HashMap<>();
		while (e.hasMoreElements()) {
			String k = e.nextElement();
			map.put(k, request.getParameter(k));
		}
		return map;
	}

	public static void sendFile(HttpServletRequest request, HttpServletResponse response, AttachVO fileInfo,
			InputStream ins) throws IOException {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			long fileSize = ins.available(); // 文件大小
			long rangeStart = 0; // 发送开始位置
			long rangeFinish = fileSize - 1; // 发送结束位置
			// 确定是否是断点续传
			String range = request.getHeader("Range");
			if (range != null && range.startsWith("bytes=")) {
				String pureRange = range.replaceAll("bytes=", "");
				int rangeSep = pureRange.indexOf("-");
				try {
					rangeStart = Long.parseLong(pureRange.substring(0, rangeSep));
					if (rangeStart > fileSize || rangeStart < 0)
						rangeStart = 0;
				} catch (NumberFormatException e) {
					// ignore the exception, keep rangeStart unchanged
				}
				if (rangeSep < pureRange.length() - 1) {
					try {
						rangeFinish = Long.parseLong(pureRange.substring(rangeSep + 1));
						if (rangeFinish < 0 || rangeFinish >= fileSize)
							rangeFinish = fileSize - 1;
					} catch (NumberFormatException e) {
						// ignore the exception
					}
				}
			}

			/***
			 * 设置文件头
			 */
			response.setHeader("Expires", "Mon, 26 Jul 1997 05:00:00 GMT");
			response.setHeader("Last-Modified",
					DateTimeUtil.formatDate(new Date(), "EEE, d MMM yyyy HH:mm:ss") + " GMT");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.setHeader("Connection", "close");
			response.setHeader("Pragma", "no-cache");
			try {
				setDownloadFileName(request, response, fileInfo.getFileName());
			} catch (Exception e) {
			}
			response.setHeader("Content-Type", "application/octet-stream;charset=UTF-8");
			response.setContentType(ContentTypeUtil.getContentType(fileInfo.getFileName()));
			ins.skip(rangeStart); // 移动文件到获取数据点
			byte buffer[] = new byte[1024];

			long len;
			int totalRead = 0;
			boolean nomore = false;
			while (true) {
				len = ins.read(buffer);
				if (len > 0 && totalRead + len > rangeFinish - rangeStart + 1) {
					len = rangeFinish - rangeStart + 1 - totalRead;
					nomore = true;
				}
				if (len > 0) {
					out.write(buffer, 0, (int) len);
					totalRead += len;
					if (nomore)
						break;
				} else {
					break;
				}
			}
			out.flush();
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ins = null;
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				out = null;
			}
		}
	}

	private static void setDownloadFileName(HttpServletRequest request, HttpServletResponse response, String fileName)
			throws Exception {
		String fileNameTemp = fileName;
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) { // 火狐
			fileNameTemp = new String(fileNameTemp.getBytes("UTF-8"), "ISO8859-1").replaceAll(" ", "-");
		} else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) { // ie
			fileNameTemp = URLEncoder.encode(fileNameTemp, "utf-8");
		} else { // 谷歌和其他的
			fileNameTemp = new String(fileNameTemp.getBytes("UTF-8"), "ISO8859-1").replaceAll(" ", "-");
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + fileNameTemp + "");
	}
}
