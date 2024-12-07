package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.service.CartService;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/cart")
@RequiredArgsConstructor
public class CustomerController {

	// 임시코드
	private final CartService cartService;
	private final JwtAuthenticationProvider jwtProvider;

	@PostMapping
	public ResponseEntity<Cart> addCart(
			@RequestHeader(name = "X-AUTH-TOKEN") String token,
			@RequestBody AddProductCartForm form) {

		return ResponseEntity.ok(cartService.addCart(jwtProvider.getUserVo(token).getId(), form));
	}
}
