package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.cms.order.exception.ErrorCode.NOT_FOUND_ITEM;
import static com.zerobase.cms.order.exception.ErrorCode.NOT_FOUND_PRODUCT;

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

	// 상품 수정
	@Transactional
	public Product updateProduct(Long sellerId, UpdateProductForm form) {
		Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
				.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

		product.setName(form.getName());
		product.setDescription(form.getDescription());

		// 옵션도 수정
		for (UpdateProductItemForm itemForm : form.getItmes()) {
			ProductItem item = product.getProductItems().stream()
					.filter( pi -> pi.getId().equals(itemForm.getItemId()))
					.findFirst().orElseThrow(() -> new CustomException(NOT_FOUND_ITEM));

			item.setName(itemForm.getName());
			item.setPrice(itemForm.getPrice());
			item.setCount(itemForm.getCount());
		}

		return product;
	}
}
