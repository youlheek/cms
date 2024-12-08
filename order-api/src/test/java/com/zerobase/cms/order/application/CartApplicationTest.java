package com.zerobase.cms.order.application;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CartApplicationTest {
// TODO : 코드 확인

	@Autowired
	private CartApplication cartApplication;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;

	@Test
	void add_and_refresh_rest() {
		Product p = add_product();
		Product result = productRepository.findWithProductItemsById(p.getId()).get();

		assertNotNull(result);

		// 나머지 필드들에 대한 검증
		assertEquals(result.getName(), "나이키 에어포스");
		assertEquals(result.getDescription(), "신발");

		assertEquals(result.getProductItems().size(), 3);
		assertEquals(result.getProductItems().get(0).getName(), "나이키 에어포스0");
		assertEquals(result.getProductItems().get(0).getPrice(), 10000);
		assertEquals(result.getProductItems().get(0).getCount(), 1);

		Long customerId = 100l;

		Cart cart = cartApplication.addCart(customerId, makeAddForm(result));

		// 데이터가 잘 들어갔는지 체크
		assertEquals(cart.getMessages().size(), 1);

	}

	AddProductCartForm makeAddForm(Product product) {
		AddProductCartForm.ProductItem productItem =
				AddProductCartForm.ProductItem.builder()
						.id(product.getProductItems().get(0).getId())
						.name(product.getProductItems().get(0).getName())
						.count(product.getProductItems().get(0).getCount())
						.build();

		return	AddProductCartForm.builder()
						.id(product.getId())
						.sellerId(product.getSellerId())
						.name(product.getName())
						.description(product.getDescription())
						.items(List.of(productItem))
						.build();
	}

	Product add_product() {
		Long sellerId = 1l;
		AddProductForm form = makeProductForm("나이키 에어포스", "신발", 3);
		return productService.addProduct(sellerId, form);
	}

	// Product 추가
	private static AddProductForm makeProductForm(String name, String description, int itemCount) {
		List<AddProductItemForm> itemForms = new ArrayList<>();

		for (int i = 0; i < itemCount; i ++) {
			itemForms.add(makeProductItemForm(null, name + i));
		}

		return AddProductForm.builder()
				.name(name)
				.description(description)
				.itmes(itemForms)
				.build();
	}

	// Product의 item 추가
	private static AddProductItemForm makeProductItemForm(Long productId, String name) {
		return AddProductItemForm.builder()
				.productId(productId)
				.name(name)
				.price(10000)
				.count(1)
				.build();
	}
}