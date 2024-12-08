//package com.zerobase.cms.order.config;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.boot.test.context.TestConfiguration;
//import redis.embedded.RedisServer;
//
//@TestConfiguration
//public class TestRedisConfig {
//	private RedisServer redisServer;
//
//	public TestRedisConfig(RedisProperties redisProperties) {
//		this.redisServer = RedisServer.builder()
//				.port(redisProperties.getPort())
//				.build();
//	}
//
//	@PostConstruct
//	public void startRedis() {
//		redisServer.start();
//	}
//	@PreDestroy
//	public void stopRedis() {
//		redisServer.stop();
//	}
//}
