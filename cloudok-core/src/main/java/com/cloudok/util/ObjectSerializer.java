package com.cloudok.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月24日 上午10:26:41
 */
public class ObjectSerializer {
	/**
	 * 序列化
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> byte[] serializer(T object) {
		if(object==null) {
			return new byte[0];
		}
		ByteArrayOutputStream bos=null;
		ObjectOutputStream oos = null;
		bos=new ByteArrayOutputStream();
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
	        return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				oos.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 反序列化
	 * @param <T>
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserializer(byte[] data) {
		if(data==null||data.length==0) {return null;}
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		bis = new ByteArrayInputStream(data);
		try {
			ois = new ObjectInputStream(bis);
			return (T) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
