package com.zerobase.cms.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.user.application.KakaoAuthApplication;
import com.zerobase.cms.user.domain.KakaoTokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class KakaoAuthController {

	private final KakaoAuthApplication kakaoAuthApplication;
	private final ObjectMapper objectMapper;

	// ✅ 카카오 로그인 요청 → 카카오 로그인 URL 리턴
	@GetMapping("/login")
	public ResponseEntity<String> kakaoLogin() {
		String kakoLoginUrl = kakaoAuthApplication.getKakaoLoginUrl();
		return ResponseEntity.ok(kakoLoginUrl);
	}

	// ✅ 카카오 로그인 후 인가 코드 받아서 처리
	@GetMapping("/callback")
	public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
		// 1. 인가 코드로 액세스 토큰 요청
		KakaoTokenResponse tokenResponse = kakaoAuthApplication.getAccessToken(code);
		String accessToken = tokenResponse.getAccessToken();

		// 2. 액세스 토큰으로 카카오 사용자 정보 조회 및 로그인 처리
		return ResponseEntity.ok(kakaoAuthApplication.getUserInfo(accessToken));
	}
}
