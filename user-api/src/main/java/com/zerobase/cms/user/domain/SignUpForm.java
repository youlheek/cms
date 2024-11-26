package com.zerobase.cms.user.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
// @Getter 이외의 어노테이션들은 사실 필요 없지만 test를 위해서 임의로 주입한다
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
// 회원가입 도메인
	private String email;
	private String name;
	private String password;
	private LocalDate birth;
	private String phone;
}
