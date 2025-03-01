package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.application.SignUpApplication;
import com.zerobase.cms.user.domain.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

	private final SignUpApplication signUpApplication;

	@PostMapping("/customer")
	// @RequestBody : 요청 본문을 DTO로 매핑하고 Swagger가 요청 본문 데이터가 필요함을 인식하게 함
	public ResponseEntity<String> customerSignUp(@RequestBody SignUpForm form) {
		return ResponseEntity.ok(signUpApplication.customerSignUp(form));
	}

	@GetMapping("/customer/verify")
	public ResponseEntity<String> customerVerify(String email, String code) {
		signUpApplication.customerVerify(email, code);
		return ResponseEntity.ok("인증이 완료되었습니다.");
	}

	@PostMapping("/seller")
	public ResponseEntity<String> sellerSignUp(@RequestBody SignUpForm form) {
		return ResponseEntity.ok(signUpApplication.sellerSignUp(form));
	}

	@GetMapping("/seller/verify")
	public ResponseEntity<String> sellerVerify(String email, String code) {
		signUpApplication.sellerVerify(email, code);
		return ResponseEntity.ok("인증이 완료되었습니다.");
	}
}
