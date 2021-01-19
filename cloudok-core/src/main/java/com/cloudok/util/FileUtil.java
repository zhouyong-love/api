package com.cloudok.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class FileUtil {

	/**
	 * 获取后缀名
	 * @param fileName
	 * @return
	 */
	public static String getSuffix(String fileName) {
		if(fileName.lastIndexOf(".")==-1) {
			return "";
		}
		return fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
	}
	
	/**
	 * 写入磁盘
	 * @param ins
	 * @param file
	 */
	public static boolean writeToDisk(InputStream ins,File file) {
		byte []temp=new byte[1024];
		int len=0;
		if(!file.getParentFile().isDirectory()) {
			file.getParentFile().mkdirs();
		}
		try(OutputStream outs=new FileOutputStream(file)) {
			while((len=ins.read(temp))>0) {
				outs.write(temp, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally {
			try {
				ins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * 删除给定目录
	 * @param absPath
	 * @return
	 */
	public static Boolean ensureExistEmptyDir(String absPath) {
        try {
        	 Files.walk(Paths.get(absPath)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }
}
