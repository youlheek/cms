package com.zerobase.cms.user.service;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {
	private final MailgunClient mailgunClient;

	public String sendEmail() {

		SendMailForm form = SendMailForm.builder()
				.from("youleenang@naver.com")
				.to("youleenang@naver.com")
				.subject("Test email from zero base")
				.text("my Test")
				.build();

		return mailgunClient.sendEmail(form).getBody();
	}
}
