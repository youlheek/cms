package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.customer.CustomerDto;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.customer.CustomerBalanceService;
import com.zerobase.cms.user.service.customer.CustomerService;
import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
// 로그인 후 고객들의 메뉴 탐방

	private final JwtAuthenticationProvider provider;
	private final CustomerService customerService;
	private final CustomerBalanceService customerBalanceService;

	// Token으로 getIntfo
	@GetMapping("/getInfo")
	public ResponseEntity<CustomerDto> getInfo(
			@RequestHeader(name = "X-AUTH-TOKEN") String token) {

		UserVo vo = provider.getUserVo(token);

		// 사실 findByIdAndEmail이 불필요함
		Customer customer = customerService.findByIdAndEmail(vo.getId(), vo.getEmail())
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		return ResponseEntity.ok(CustomerDto.from(customer));
	}

	@PostMapping("/balance")
	public ResponseEntity<Integer> changeBalance(
			@RequestHeader(name = "X-AUTH-TOKEN") String token,
			@RequestBody ChangeBalanceForm form) {

		UserVo vo = provider.getUserVo(token);

		return ResponseEntity.ok(
				customerBalanceService.changeBalance(vo.getId(), form).getCurrentMoney());
	}

