package com.zerobase.cms.order.domain.redis;

import com.zerobase.cms.order.domain.product.AddProductCartForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor @AllArgsConstructor
@RedisHash("cart") // 특정 클래스 ("cart")가 Redis에 저장될 수 있는 객체임을 나타냄
public class Cart {

	@Id
	private Long customerId;
	private List<Product> products = new ArrayList<>();
	private List<String> messages = new ArrayList<>();

	public Cart(Long customerId) {
		this.customerId = customerId;
	}

	public void addMessage(String message) {
		messages.add(message);
	}

	@Data
	@Builder
	@NoArgsConstructor @AllArgsConstructor
	public static class Product{
		private Long id;
		private Long sellerId;
		private String name;
		private String description;
		private List<ProductItem> items = new ArrayList<>();

		public static Product from(AddProductCartForm form) {
			return Product.builder()
					.id(form.getId())
					.sellerId(form.getSellerId())
					.name(form.getName())
					.description(form.getDescription())
					.items(form.getItems()
							.stream()
							.map(ProductItem::from)
							.collect(Collectors.toList()))
					.build();
		}
	}

	@Data
	@Builder
	@NoArgsConstructor @AllArgsConstructor
	public static class ProductItem {
		private Long id;
		private String name;
		private Integer count;
		private Integer price;

		public static ProductItem from(AddProductCartForm.ProductItem form) {
			return ProductItem.builder()
					.id(form.getId())
					.name(form.getName())
					.count(form.getCount())
					.price(form.getPrice())
					.build();
		}
	}

	// 카피에 딥카피와 얕은카피가 있는데 우리는 얕은카피로 할 것임
	// 인스턴스를 새로 만들지 않고 재활용하는 방식으로 쓸 것
	// TODO : 카피에 대해 알아보자
//	public Cart clone() {
//	}
}
