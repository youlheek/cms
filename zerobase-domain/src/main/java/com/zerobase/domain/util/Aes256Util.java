package com.zerobase.domain.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

// 암호화/복호화를 하기 위한 클래스
public class Aes256Util {

	public static String alg = "AES/CBC/PKCS5Padding";
	private static final String KEY = "ZEROBASEKEY_ISZEROBASE_KEY123456";
	private static final String IV = KEY.substring(0, 16);

	public static String encrypt(String text) {
		try {
			Cipher cipher = Cipher.getInstance(alg);
			SecretKey keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

			byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

			return Base64.encodeBase64String(encrypted);

		} catch (Exception e) {
			e.printStackTrace();  // 디버깅에 도움이 되도록 오류를 출력합니다.

			return null;
		}
	}

	public static String decrypt(String cipherText) {
		try {
			Cipher cipher = Cipher.getInstance(alg);
			SecretKey keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

			byte[] decodedBytes = Base64.decodeBase64(cipherText);
			byte[] decrypted = cipher.doFinal(decodedBytes);

			return new String(decrypted, StandardCharsets.UTF_8);

		} catch (Exception e) {
			return null;
		}


	}
}
