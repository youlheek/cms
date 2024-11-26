package com.zerobase.cms.user.service;

import com.zerobase.cms.user.config.FeignConfig;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailSendServiceTest {

	@Autowired
	private EmailSendService emailSendService;

	@Test
	public void emailTest() {
		String response = emailSendService.sendEmail();
		System.out.println(response);
	    //given
	    //when
	    //then
	}

}