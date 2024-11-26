package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

	private final CustomerRepository customerRepository;

	// [고객] 회원가입
	public Customer signUp(SignUpForm form) {
		return customerRepository.save(Customer.from(form));
	}
}
