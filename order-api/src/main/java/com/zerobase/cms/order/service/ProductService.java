package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
// 상품 등록, 추가
	private final ProductRepository productRepository;

	// 상품 추가
	@Transactional
	public Product addProduct(Long sellerId, AddProductForm form) {
		return productRepository.save(Product.of(sellerId, form));
	}
}
