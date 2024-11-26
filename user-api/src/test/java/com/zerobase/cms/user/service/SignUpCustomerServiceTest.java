package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignUpCustomerServiceTest {

	@Autowired
	private SignUpCustomerService signUpCustomerService;

	@Test
	void signUp() {
//		ㅈ ㅏ 생각해보자
//		지금 하고싶은게 뭐야? service를 테스트해보고싶은거잖아
//		그럼 뭐가 필요해? form이 필요하지
		SignUpForm form = SignUpForm.builder()
				.email("abc@gmail.com")
				.name("youlhee")
				.password("1")
				.birth(LocalDate.now())
				.phone("01000000000")
				.build();

		Customer customer = signUpCustomerService.signUp(form);
		assertNotNull(signUpCustomerService.signUp(form).getId());

	}
}