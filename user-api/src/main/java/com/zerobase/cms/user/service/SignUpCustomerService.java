package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static com.zerobase.cms.user.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {
// 회원가입

	private final CustomerRepository customerRepository;

	// [고객] 회원가입 - 폼 제출
	public Customer signUp(SignUpForm form) {
		return customerRepository.save(Customer.from(form));
	}

	// [고객] 회원가입 - 이메일 중복 확인
	public boolean isEmailExist(String email) {
		return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
				.isPresent();
	}

	@Transactional
	// email에 대한 verify 처리
	public void verifyEmail(String email, String code) {
		Customer customer = customerRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
		if (customer.isVerify()) { // 📍 isVerify를 어떻게 쓸 수 있는거지?
			throw new CustomException(ALREADY_VERIFY);
		} else if (!customer.getVerificationCode().equals(code)) {
			throw new CustomException(WRONG_VERIFICATION);
		}else if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
			throw new CustomException(EXPIRE_CODE);
		}

		customer.setVerify(true);
	}

	@Transactional
	//
	public LocalDateTime changeCustomerValidateEmail(Long customerId, String vericationCode) {
		Optional<Customer> customerOptional = customerRepository.findById(customerId);

		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			customer.setVerificationCode(vericationCode);
			customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));

			return customer.getVerifyExpiredAt();
		}

		throw new CustomException(NOT_FOUND_USER);
	}
}