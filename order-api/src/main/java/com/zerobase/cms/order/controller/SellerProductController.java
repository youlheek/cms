package com.zerobase.cms.order.controller;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.*;
import com.zerobase.cms.order.service.ProductItemService;
import com.zerobase.cms.order.service.ProductService;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductController {
	// 상품등록
	private final ProductService productService;
	private final ProductItemService productItemService;
	private final JwtAuthenticationProvider jwtProvider;

	@PostMapping
	public ResponseEntity<ProductDto> addProduct(
			@RequestHeader(name = "X-AUTH-TOKEN") String token,
			@RequestBody AddProductForm form) {

		return ResponseEntity.ok(
				ProductDto.from(
						productService.addProduct(jwtProvider.getUserVo(token).getId(), form)));
	}

	@PostMapping("/item")
	public ResponseEntity<ProductDto> addProductItem(
			@RequestHeader(name = "X-AUTH-TOKEN") String token,
			@RequestBody AddProductItemForm form) {

		return ResponseEntity.ok(
				ProductDto.from(
						productItemService.addProductItem(jwtProvider.getUserVo(token).getId(), form)));
	}

	@PutMapping
	public ResponseEntity<ProductDto> updateProduct(
			@RequestHeader(name = "X-AUTH-TOKEN") String token,
			@RequestBody UpdateProductForm form) {

		return ResponseEntity.ok(
				ProductDto.from(
						productService.updateProduct(jwtProvider.getUserVo(token).getId(), form)));
	}

	@PutMapping("/item")
	public ResponseEntity<ProductItemDto> updateProductItem(
			@RequestHeader(name = "X-AUTH-TOKEN") String token,
			@RequestBody UpdateProductItemForm form) {

		return ResponseEntity.ok(
				ProductItemDto.from(
						productItemService.updateProductItem(jwtProvider.getUserVo(token).getId(), form)));

	}
}
