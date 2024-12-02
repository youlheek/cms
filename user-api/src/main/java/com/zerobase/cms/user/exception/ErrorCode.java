package com.zerobase.cms.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
	// Bad_request가 맞아..?
	NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 회원이 없습니다."),
	ALREADY_VERIFY(HttpStatus.BAD_REQUEST,"이미 인증이 완료되었습니다."),
	EXPIRE_CODE(HttpStatus.BAD_REQUEST,"인증 시간이 만료되었습니다."),
	WRONG_VERIFICATION(HttpStatus.BAD_REQUEST,"잘못된 인증 시도입니다."),

	// Login
	LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "아이디와 패스워드를 확인해 주세요."),

	;


	private final HttpStatus httpStatus;
	private final String detail;
}
