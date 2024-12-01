package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.applicatoin.SignInApplication;
import com.zerobase.cms.user.domain.SignInForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/signIn")
@RequiredArgsConstructor
public class SignInController {

	private final SignInApplication signInApplication;

	// [고객] - 로그인 및 토큰 발행
	@PostMapping("/customer")
	public ResponseEntity<String> signInCustomer(@RequestBody SignInForm form) {
		// 토큰 발행
		// 로그인 처리

		return ResponseEntity.ok(signInApplication.customerLogInToken(form));
	}

	// [셀러] - 로그인 및 토큰 발행
	@PostMapping("/seller")
	public ResponseEntity<String> signInSeller(@RequestBody SignInForm form) {
		// 로그인 처리 및 토큰 발행
		return ResponseEntity.ok(signInApplication.sellerLogInToken(form));
	}
}
