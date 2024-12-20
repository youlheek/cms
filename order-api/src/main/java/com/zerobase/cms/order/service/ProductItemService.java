package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductItemRepository;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.cms.order.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ProductItemService {
	private final ProductRepository productRepository;
	private final ProductItemRepository productItemRepository;

	// 상품 옵션 추가
	@Transactional
	public Product addProductItem(Long sellerId, AddProductItemForm form) {
		Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
				.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

		// 상품옵션명 중복일 경우
		if (product.getProductItems().stream()
				.anyMatch(item -> item.getName().equals(form.getName()))) {
			throw new CustomException(SAME_ITEM_NAME);
		}

		ProductItem productItem = ProductItem.of(sellerId, form);
		product.getProductItems().add(productItem);
		return product;
	}

	// 상품 옵션 수정
	@Transactional
	public ProductItem updateProductItem(Long sellerId, UpdateProductItemForm form) {
		ProductItem productItem = productItemRepository.findById(form.getItemId())
				.filter(pi -> pi.getSellerId().equals(sellerId))
				.orElseThrow(() -> new CustomException(NOT_FOUND_ITEM));

		productItem.setName(form.getName());
		productItem.setPrice(form.getPrice());
		productItem.setCount(form.getCount());

		return productItem;
	}

	// 상품 옵션 삭제
	@Transactional
	public void deleteProductItem(Long sellerId, Long productItemId) {
		ProductItem productItem = productItemRepository.findById(productItemId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_ITEM));

		productItemRepository.delete(productItem);
	}

	@Transactional // TODO : 굳이 여기다가도 Transacational 을 써야하나?
	public ProductItem getProductItem(Long id) {
		return productItemRepository.getById(id);
	}

}
