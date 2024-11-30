package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.domain.customer.CustomerDto;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.CustomerService;
import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
// 로그인 후 고객들의 메뉴 탐방

	private final JwtAuthenticationProvider provider;
	private final CustomerService customerService;

	// 회원 정보
	@GetMapping("/getInfo")
	public ResponseEntity<CustomerDto> getInfo(
			@RequestHeader(name = "X-AUTH-TOKEN") String token)  {

		UserVo vo = provider.getUserVo(token);

		// 사실 findByIdAndEmail이 불필요함
		Customer customer = customerService.findByIdAndEmail(vo.getId(), vo.getEmail())
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		return ResponseEntity.ok(CustomerDto.from(customer));
	}

}
