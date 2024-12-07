package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zerobase.cms.order.exception.ErrorCode.NOT_FOUND_PRODUCT;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
	private final ProductRepository productRepository;

	// 상품명 검색
	// TODO : 추후 검색 엔진 달아서 빌드업 할 수 있을듯
	public List<Product> searchByName(String name) {
		return productRepository.searchByName(name);
	}

	// 상품 코드 검색 API - 상품 코드를 통해
	public Product getByProductId(Long productId) {
		return productRepository.findWithProductItemsById(productId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
	}

	// 목록 검색
	public List<Product> getListByProducId(List<Long> productIds) {
		return productRepository.findAllById(productIds);
	}
}
