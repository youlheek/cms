package com.zerobase.cms.user.service;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 이 클래스는 email 인증을 위한 테스트 클래스였기 때문에 삭제조치함
 */
@Service
@RequiredArgsConstructor
public class EmailSendService {
	private final MailgunClient mailgunClient;

	public String sendEmail() {

		SendMailForm form = SendMailForm.builder()
				.from("youleenang@naver.com")
				.to("youlheek@gmail.com")
				.subject("Test email from zero base")
				.text("my Test")
				.build();

		return mailgunClient.sendEmail(form).getBody();
	}
}
