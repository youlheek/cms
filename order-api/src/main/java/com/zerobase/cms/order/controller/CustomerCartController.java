package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.application.CartApplication;
import com.zerobase.cms.order.application.OrderApplication;
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
// 왜 어떤건 url 매핑을 하고 어떤건 안할까?

	// 임시코드
	private final JwtAuthenticationProvider jwtProvider;
	private final CartApplication cartApplication;
	private final OrderApplication orderApplication;

	// 장바구니 추가
	@PostMapping
	public ResponseEntity<Cart> addCart(
			@RequestHeader(name = "X-AUTH-TOKEN") String token,
			@RequestBody AddProductCartForm form) {

		return ResponseEntity.ok(cartApplication.addCart(jwtProvider.getUserVo(token).getId(), form));
	}

	// 장바구니 보기
	@GetMapping
	public ResponseEntity<Cart> showCart(
			@RequestHeader(name = "X-AUTH-TOKEN") String token) {

		return ResponseEntity.ok(cartApplication.getCart(jwtProvider.getUserVo(token).getId()));
	}

	// 현업에서는 DTO등으로 wrapping해서 나가는 게 좋음
	// 장바구니 변경
	@PutMapping
	public ResponseEntity<Cart> updateCart(
			@RequestHeader(name = "X-AUTH-TOKEN") String token,
			@RequestBody Cart cart) {

		return ResponseEntity.ok(cartApplication.updateCart(jwtProvider.getUserVo(token).getId(), cart));
	}

	// TODO : 근데 애초에 Cart를 제출할게 아니라 Redis에 있는 걸 불러와서 결제해야하는거 아닌가..?
	// token + product, item id 를 제출해서
	// Redis 와 DB 에 있는 수량과 가격을 비교한다음
	// Redis 의 정보를 새로고침하고 message 띄우고 리턴시켜야 하는거 아닌지
	@PostMapping("/order")
	public ResponseEntity<Cart> order(
			@RequestHeader(name = "X-AUTH-TOKEN") String token,
			@RequestBody Cart cart) {

		orderApplication.order(token, cart);
		return ResponseEntity.ok().build();
		// TODO : 주문이 완성되면 뭔가 보여줘야할것같은데 balance 라던가...?
	}

	/**
	 * 현재 ORDER 정책
	 * 1. token 제출
	 * 2. 주문서 : product&item Id, price, count 제출
	 * 3. DB의 정보와 주문서의 정보들과 비교
	 * 4. User money, Db 수량 수정
	 *
	 * 여기서 Redis 는 아무 쓸모가 없어
	 */
}
