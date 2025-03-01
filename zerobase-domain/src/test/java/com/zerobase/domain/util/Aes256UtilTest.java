package com.zerobase.domain.util;

import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class Aes256UtilTest {

	@Test
	void encrypt() {
		String encrypt = Aes256Util.encrypt("Hello World");
		String decrypt = Aes256Util.decrypt(encrypt);
		assertEquals(decrypt, "Hello World");
	}

	@Test
	void solution() {

		int n = 3;
		String answer = "";



		assertEquals("we", answer);

		//test3
	}
}