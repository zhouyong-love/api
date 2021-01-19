package com.cloudok.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author xiazhijian
 *
 */
public class AESUtil {

	public static byte[] encoder(byte[] sorce, byte[] secret) {
		try {
			Cipher aesECB = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec key = new SecretKeySpec(secret, "AES");
			aesECB.init(Cipher.ENCRYPT_MODE, key);
			return aesECB.doFinal(sorce);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] decoder(byte[] sorce, byte[] secret) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec key = new SecretKeySpec(secret, "AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(sorce);
		} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException
				| NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
