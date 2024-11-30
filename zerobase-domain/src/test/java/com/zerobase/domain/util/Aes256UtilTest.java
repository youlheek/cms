package com.zerobase.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Aes256UtilTest {

	@Test
	void encrypt() {
		String encrypt = Aes256Util.encrypt("Hello World");
		String decrypt = Aes256Util.decrypt(encrypt);
		assertEquals(decrypt, "Hello World");
	}
}