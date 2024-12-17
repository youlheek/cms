package com.zerobase.cms.user.applicatoin;

import com.zerobase.cms.user.domain.AuthResponse;
import com.zerobase.cms.user.domain.SignInForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.service.customer.CustomerService;
import com.zerobase.cms.user.service.seller.SellerService;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.zerobase.cms.user.exception.ErrorCode.LOGIN_CHECK_FAIL;
import static com.zerobase.domain.common.UserType.CUSTOMER;
import static com.zerobase.domain.common.UserType.SELLER;

@Service
@RequiredArgsConstructor
public class SignInApplication {

	private final CustomerService customerService;
	private final SellerService sellerService;
	private final JwtAuthenticationProvider provider;
	private final RedisTemplate<String, String> redisTemplate;

	public AuthResponse customerLogInToken(SignInForm form, HttpServletResponse response) {
		// 1. 로그인 가능 여부
		Customer customer = customerService.findValidCustomer(form.getEmail(), form.getPassword())
				.orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

		// 2. Access & Refresh Token 발급
		String accessToken = provider.createAccessToken(customer.getEmail(), customer.getId(), CUSTOMER);
		String refreshToken = provider.createRefreshToken(customer.getEmail(), customer.getId(), CUSTOMER);

		// 3. Redis에 Refresh Token 저장
		redisTemplate.opsForValue().set(form.getEmail(), refreshToken, 7, TimeUnit.DAYS);


		// 4. Refresh Token을 HttpOnly Cookie에 설정
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7));
		response.addCookie(cookie);

		// 5. 토큰을 Response
		return new AuthResponse(accessToken);
	}

	// 로그인 및 토큰 발행
	public String sellerLogInToken(SignInForm form) {
		// 1. 로그인 (email, password)
		Seller seller = sellerService.findValidSeller(form.getEmail(), form.getPassword())
				.orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

		// 2. 토큰 발행 및 return
		return provider.createAccessToken(seller.getEmail(), seller.getId(), SELLER);
	}
}
