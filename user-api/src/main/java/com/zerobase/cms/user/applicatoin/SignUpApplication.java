package com.zerobase.cms.user.applicatoin;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.SignUpCustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignUpApplication {
// 회원가입 시 필요한 기능들 만들기

	private final MailgunClient mailgunClient;
	private final SignUpCustomerService signUpCustomerService;


	// 회원가입 폼 제출
	public String customerSignUp(SignUpForm form) {
		if(signUpCustomerService.isEmailExist(form.getEmail())) { // 이메일이 중복일때
			// exception
			throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
		} else {
			// 회원가입 - DB에 save
			Customer c = signUpCustomerService.signUp(form);
			LocalDateTime now = LocalDateTime.now();
			String code = getRandomCode();

			SendMailForm sendMailForm = SendMailForm.builder()
					.from("test@mytester.com")
					.to(c.getEmail())
					.subject("verification Email!")
					.text(getVerificationEmailBody(c.getEmail(), c.getName(), code))
					.build();

			mailgunClient.sendEmail(sendMailForm);
			signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);

			return "회원가입에 성공하였습니다";
		}
	}

	// 회원가입 인증
	public void customerVerify(String email, String code) {
		signUpCustomerService.verifyEmail(email, code);
	}

	// 인증 코드 만들기
	private String getRandomCode() {
		return RandomStringUtils.random(10, true, true);
	}

	// 이메일 만들기
	private String getVerificationEmailBody(String email, String name, String code) {
		StringBuilder builder = new StringBuilder();

		return builder
				.append("Hello")
				.append(name)
				.append("! Please Click Link for verification.\n\n")
				.append("http://localhost:8081/signup/verify/customer?email=")
				.append(email)
				.append("&code=")
				.append(code).toString();
	}
}
