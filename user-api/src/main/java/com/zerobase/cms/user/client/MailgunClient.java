package com.zerobase.cms.user.client;

import com.zerobase.cms.user.client.mailgun.SendMailForm;
import feign.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url="https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {

	@PostMapping("sandbox735d3852911b4d81a04856e9dae33c86.mailgun.org/messages")
	ResponseEntity<String> sendEmail(@SpringQueryMap SendMailForm form);
}
