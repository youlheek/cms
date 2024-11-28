package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.applicatoin.SignUpApplication;
import com.zerobase.cms.user.domain.SignUpForm;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

	private final SignUpApplication signUpApplication;

	@PostMapping
	// @RequestBody : 요청 본문을 DTO로 매핑하고 Swagger가 요청 본문 데이터가 필요함을 인식하게 함
	public ResponseEntity<String> customerSignUp(@RequestBody SignUpForm form) {
		return ResponseEntity.ok(signUpApplication.customerSignUp(form));
	}

	// todo : swagger 통해서 url 테스트 해보기 - getVerificationEmailBody 참고
	@PutMapping("/verify/customer")
	public ResponseEntity<String> verifyCustomer(String email, String code) {
		signUpApplication.customerVerify(email, code);
		return ResponseEntity.ok("인증이 완료되었습니다.");
	}
}
