package com.zerobase.cms.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients // 다른 모듈과 통신을 위해 ?
@EnableJpaAuditing
@EnableJpaRepositories(repositoryBaseClass = EnversRevisionRepositoryFactoryBean.class)
@ServletComponentScan
public class ZeroOrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZeroOrderApplication.class);
	}
}

