package com.zerobase.cms.user.domain;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignUpForm {
// 회원가입 도메인
	private String email;
	private String name;
	private String password;
	private LocalDate birth;
	private String phone;
}
