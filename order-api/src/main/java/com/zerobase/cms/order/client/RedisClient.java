package com.zerobase.cms.order.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static com.zerobase.cms.order.exception.ErrorCode.CART_CHANGE_FAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisClient {

	private final RedisTemplate<String, Object> redisTemplate;
	private static final ObjectMapper mapper = new ObjectMapper();
	// ObjectMapper : 데이터를 JSON 문자열로 변환하거나, JSON 문자열을 객체로 변환하는 Jackson 라이브러리의 도구

	public <T> T get(Long key, Class<T> classType) {
		return get(key.toString(), classType);
	}

	// 카트 가져오기
	public <T> T get(String key, Class<T> classType) {
		String redisValue = (String) redisTemplate.opsForValue().get(key);
		// Redis에서 키에 해당하는 값을 가져와서 JSON 문자열(Object)로 반환

		if (ObjectUtils.isEmpty(redisValue)) {
			return null;
		} else {
			try {
				return mapper.readValue(redisValue, classType);
				// json 문자열을 classType의 객체로 변환하여 반환
			} catch (JsonProcessingException e) {
				log.error("Parsing error", e);
				return null;
			}
		}
	}

	public void put(Long key, Cart cart) {
		put(key.toString(), cart);
	}

	// 카트에 넣기
	public void put(String key, Cart cart) {

		try {
			redisTemplate.opsForValue().set(key, mapper.writeValueAsString(cart));
		} catch (JsonProcessingException e) {
			throw new CustomException(CART_CHANGE_FAIL);
		}
	}

}
