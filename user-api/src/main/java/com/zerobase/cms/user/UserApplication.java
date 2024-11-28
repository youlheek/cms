package com.zerobase.cms.user;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@SpringBootApplication
// 📍 SignUpCustomerService테스트를 위해 밑에 어노테이션들을 추가했는데 그 이유가 뭘까
@EnableJpaAuditing
@EnableJpaRepositories
@RequiredArgsConstructor
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
