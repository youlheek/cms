package com.zerobase.cms.user.service;

import com.zerobase.cms.user.client.MailgunClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendServiceTest {

	@Autowired
//	private EmailSendService emailSendService;
	private MailgunClient mailgunClient;

	@Test
	public void emailTest() {
//		String response = emailSendService.sendEmail();
//		System.out.println(response);

		// todo : need test code
		mailgunClient.sendEmail(null);
	    //given
	    //when
	    //then
	}

}