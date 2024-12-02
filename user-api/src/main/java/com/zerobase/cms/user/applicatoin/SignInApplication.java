package com.zerobase.cms.user.applicatoin;

import com.zerobase.cms.user.domain.SignInForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.service.customer.CustomerService;
import com.zerobase.cms.user.service.seller.SellerService;
import com.zerobase.domain.common.UserType;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.zerobase.cms.user.exception.ErrorCode.LOGIN_CHECK_FAIL;
import static com.zerobase.domain.common.UserType.CUSTOMER;
import static com.zerobase.domain.common.UserType.SELLER;

@Service
@RequiredArgsConstructor
public class SignInApplication {

	private final CustomerService customerService;
	private final SellerService sellerService;
	private final JwtAuthenticationProvider provider;

	public String customerLogInToken(SignInForm form) {
		// 1. 로그인 가능 여부
		Customer customer = customerService.findValidCustomer(form.getEmail(), form.getPassword())
				.orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

		// 2. 토큰을 발행

		// 3. 토큰을 Response 한다
		return provider.createToken(customer.getEmail(), customer.getId(), CUSTOMER);
	}

	// 로그인 및 토큰 발행
	public String sellerLogInToken(SignInForm form)	{
		// 1. 로그인 (email, password)
		Seller seller = sellerService.findValidSeller(form.getEmail(), form.getPassword())
				.orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

		// 2. 토큰 발행 및 return
		return provider.createToken(seller.getEmail(), seller.getId(), SELLER);
	}
}
