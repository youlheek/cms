package com.zerobase.cms.user.applicatoin;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.customer.SignUpCustomerService;
import com.zerobase.cms.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.zerobase.cms.user.exception.ErrorCode.ALREADY_REGISTERED_USER;

@Service
@RequiredArgsConstructor
public class SignUpApplication {
// 회원가입 시 필요한 기능들 만들기

	private final MailgunClient mailgunClient;
	private final SignUpCustomerService signUpCustomerService;
	private final SellerService sellerService;

	// [고객] 회원가입 폼 제출
	public String customerSignUp(SignUpForm form) {
		// 이메일 중복 확인
		if(signUpCustomerService.isEmailExist(form.getEmail())) {
			// exception
			throw new CustomException(ALREADY_REGISTERED_USER);
		} else {
			// 회원가입 - DB에 save
			Customer customer = signUpCustomerService.signUp(form);
			LocalDateTime now = LocalDateTime.now();

			// 인증코드 만들기
			String code = getRandomCode();

			// 메일로 인증코드 발송
			SendMailForm sendMailForm = SendMailForm.builder()
					.from("test@mytester.com")
					.to(customer.getEmail())
					.subject("verification Email!")
					.text(getVerificationEmailBody(customer.getEmail(), customer.getName(), "customer", code))
					.build();

			mailgunClient.sendEmail(sendMailForm);
			signUpCustomerService.changeCustomerValidateEmail(customer.getId(), code);

			return "회원가입에 성공하였습니다";
		}
	}

	// [셀러] 회원가입 폼 제출
	public String sellerSignUp(SignUpForm form) {
		// 이미 가입된 메일인지 체크
		if(sellerService.isEmailExist(form.getEmail())) {
			throw new CustomException(ALREADY_REGISTERED_USER);
		}
		// 회원가입 - DB에 save
		Seller seller = sellerService.signUp(form);

		// 메일로 인증코드 발송
		String code = getRandomCode();
		SendMailForm mailForm = SendMailForm.builder()
				.from("test@mytester.com")
				.to(seller.getEmail())
				.subject("verification Email!")
				.text(getVerificationEmailBody(seller.getEmail(), seller.getName(), "seller", code))
				.build();
		mailgunClient.sendEmail(mailForm);

		// 인증코드 및 만료일 DB에 저장
		LocalDateTime expiredAt = sellerService.changeSellerValidationEmail(seller.getId(), code);

		return "회원가입에 성공하였습니다. 메일로 인증코드를 확인해주십시오. 인증 만료일은 "+ expiredAt + "입니다";
	}

	// TODO : 밑에 공통 기능 코드들 리팩토링 진행 (메소드 명 변경 포함)
	// 회원가입 인증
	public void customerVerify(String email, String code) {
		signUpCustomerService.verifyEmail(email, code);
	}

	public void sellerVerify(String email, String code) {
		sellerService.verifyEmail(email, code);
	}

	// 인증 코드 만들기
	private String getRandomCode() {
		return RandomStringUtils.random(10, true, true);
	}

	// 이메일 만들기
	private String getVerificationEmailBody(String email, String name, String type, String code) {
		StringBuilder builder = new StringBuilder();

		return builder
				.append("Hello ")
				.append(name)
				.append("! Please Click Link for verification.\n\n")
				.append("http://localhost:8081/signup/"+type+"/verify?email=")
				.append(email)
				.append("&code=")
				.append(code).toString();
	}

	// 📍 같은 비즈니스로직 같아보이는데 어떤건 Applicationd에 넣고 어떤건 Service에 넣을까?
}
