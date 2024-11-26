package com.zerobase.cms.user.service;

import com.zerobase.cms.user.client.MailgunClient;
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
//	private EmailSendService emailSendService;
	private MailgunClient mailgunClient;

	@Test
	public void emailTest() {
//		String response = emailSendService.sendEmail();
//		System.out.println(response);

		// 📍 숙제 need test code
		mailgunClient.sendEmail(null);
	    //given
	    //when
	    //then
	}

}