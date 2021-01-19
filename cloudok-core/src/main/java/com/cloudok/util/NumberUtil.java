package com.cloudok.util;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月15日 下午9:58:10
 */
public class NumberUtil {

	/**
	 * Object 2 int
	 * 
	 * @param o
	 * @return
	 */
	public static int tryToInt(Object o) {
		return tryToInt(o, 0);
	}
	
	/**
	 * Object 2 int
	 * @param o
	 * @param _default
	 * @return
	 */
	public static int tryToInt(Object o,int _default) {
		if (o == null) {
			return 0;
		}
		try {
			return Integer.parseInt(o.toString());
		} catch (NumberFormatException e) {
			return _default;
		}
	}

	/**
	 * Object 2 Integer
	 * 
	 * @param o
	 * @return
	 */
	public static Integer tryToInteger(Object o) {
		if (o == null) {
			return null;
		}
		try {
			return Integer.parseInt(o.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * long 2 byte
	 * 
	 * @param x
	 * @return
	 */
	public static byte[] longToBytes(long x) {
		byte[] b = new byte[8];
		for (byte i = 0; i < 8; i++) {
			b[i] = (byte) ((x >> ((i) * 8)) & 0xff);
		}
		return b;
	}

	/**
	 * byte 2 long
	 * 
	 * @param bytes
	 * @return
	 */
	public static long bytesToLong(byte[] bytes) {
		if (bytes.length < 8) {
			throw new IndexOutOfBoundsException();
		}
		long l = 0L;
		for (byte i = 0; i < bytes.length && i < 8; i++) {
			l |= ((long) (bytes[i] & 0xff)) << (i * 8);
		}
		return l;
	}

	/**
	 * byte 2 long
	 * 
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static long bytesToLong(byte[] bytes, int offset) {
		if (bytes.length < offset + 8) {
			throw new IndexOutOfBoundsException();
		}
		long l = 0L;
		for (int i = offset; i < bytes.length && i < 8 + offset; i++) {
			l |= ((long) (bytes[i] & 0xff)) << ((i - offset) * 8);
		}
		return l;
	}

	/**
	 * int 2 bytes
	 * 
	 * @param x
	 * @return
	 */
	public static byte[] intToBytes(int x) {
		byte[] b = new byte[4];
		for (byte i = 0; i < 4; i++) {
			b[i] = (byte) ((x >> ((i) * 8)) & 0xff);
		}
		return b;
	}

	/**
	 * bytes 2 int
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte... bytes) {
		if (bytes.length < 4) {
			throw new IndexOutOfBoundsException();
		}
		int l = 0;
		for (byte i = 0; i < bytes.length && i < 4; i++) {
			l |= ((int) (bytes[i] & 0xff)) << ((i) * 8);
		}
		return l;
	}

	/**
	 * bytes 2 int
	 * 
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static int bytesToInt(byte[] bytes, int offset) {
		if (bytes.length < offset + 4) {
			throw new IndexOutOfBoundsException();
		}
		int l = 0;
		for (int i = offset; i < bytes.length && i < 4 + offset; i++) {
			l |= ((int) (bytes[i] & 0xff)) << ((i - offset) * 8);
		}
		return l;
	}

	/**
	 * short 2 bytes
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] short2Bytes(short s) {
		byte b[] = new byte[2];
		b[1] = (byte) ((s >> 8) & 0xff);
		b[0] = (byte) (s & 0xff);
		return b;
	}

	/**
	 * bytes 2 short
	 * 
	 * @param b
	 * @return
	 */
	public static short bytes2short(byte... b) {
		if (b.length < 2) {
			throw new IndexOutOfBoundsException();
		}
		return (short) (((b[1] & 0xff) << 8) | (b[0] & 0xff));
	}

	/**
	 * bytes 2 short
	 * 
	 * @param b
	 * @param offset
	 * @return
	 */
	public static short bytes2short(byte[] b, int offset) {
		if (b.length < 2 + offset) {
			throw new IndexOutOfBoundsException();
		}
		return (short) (((b[offset + 1] & 0xff) << 8) | (b[offset] & 0xff));
	}

	/**
	 * string 2 long
	 * @param num
	 * @return
	 */
	public static long try2Long(String num) {
		return try2Long(num,0L);
	}
	
	/**
	 * string 2 long
	 * @param num
	 * @param _default
	 * @return
	 */
	public static long try2Long(String num, long _default) {
		try {
			return Long.parseLong(num);
		}catch (Exception e) {
			return _default;
		}
	}
}