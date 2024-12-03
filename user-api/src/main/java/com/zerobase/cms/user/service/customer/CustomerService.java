package com.zerobase.cms.user.service.customer;

import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;

	// id로 Customer 객체 찾기
	public Optional<Customer> findByIdAndEmail(Long id, String email) {
		return customerRepository.findById(id)
				.stream()
				.filter( customer -> customer.getEmail().equals(email) )
				.findFirst();
		// TODO : 📍 왜 고유한 id로 검색하면서 email로 filter를 한번 더 걸러내는가?
	}

	// 1. 로그인 가능 여부 체크
	public Optional<Customer> findValidCustomer(
			String email, String password) {
		return customerRepository.findByEmail(email)
				.stream()
				.filter(
						customer -> customer.getPassword().equals(password)
						&& customer.isVerify())
				.findFirst();
	}


}
