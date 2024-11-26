package com.zerobase.cms.user;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

@EnableFeignClients
@SpringBootApplication
public class UserApplication {

	@Autowired
	private Environment env;

	@PostConstruct
	public void checkMailgunKey() {
		String mailgunKey = env.getProperty("mailgun.key");
		System.out.println(mailgunKey);
	}

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
}
