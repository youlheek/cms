package com.zerobase.cms.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

	NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
	NOT_FOUND_ITEM(HttpStatus.BAD_REQUEST, "옵션을 찾을 수 없습니다."),
	SAME_ITEM_NAME(HttpStatus.BAD_REQUEST, "중복된 아이템 명 입니다."),

	CART_CHANGE_FAIL(HttpStatus.BAD_REQUEST, "장바구니에 추가할 수 없습니다."),
	ITEM_COUNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품의 수량이 부족합니다."),

	ORDER_FAIL_CART_CHECK(HttpStatus.BAD_REQUEST, "주문 불가! 장바구니를 확인해 주세요."),
	ORDER_FAIL_ENOUGH_MONEY(HttpStatus.BAD_REQUEST, "주문 불가! 잔액을 확인해 주세요."),
	;

	private final HttpStatus httpStatus;
	private final String detail;
}
