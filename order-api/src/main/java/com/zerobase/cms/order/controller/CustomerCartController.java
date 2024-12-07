package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.application.CartApplication;
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
public class CustomerCartController {

	// 임시코드
	private final JwtAuthenticationProvider jwtProvider;
	private final CartApplication cartApplication;

	@PostMapping
	public ResponseEntity<Cart> addCart(
			@RequestHeader(name = "X-AUTH-TOKEN") String token,
			@RequestBody AddProductCartForm form) {

		return ResponseEntity.ok(cartApplication.addCart(jwtProvider.getUserVo(token).getId(), form));
	}

	@GetMapping
	public ResponseEntity<Cart> showCart(
			@RequestHeader(name = "X-AUTH-TOKEN") String token) {

		return ResponseEntity.ok(cartApplication.getCart(jwtProvider.getUserVo(token).getId()));
	}
}
