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

		int[] a = {1,2,3,4};
		int[] b = {-3,-1,0,2};
		int answer = 0;


		answer = IntStream.range(0, a.length).map(index -> a[index]*b[index]).sum();

		assertEquals(3, answer);

		//test3
	}
}