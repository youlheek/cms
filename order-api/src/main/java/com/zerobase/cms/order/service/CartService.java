package com.zerobase.cms.order.service;

import com.zerobase.cms.order.client.RedisClient;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {
// TODO : 코드 확인

	private final RedisClient redisClient;

	// 장바구니 가져오기
	public Cart getCart(Long customerId) {
		return redisClient.get(customerId, Cart.class);
	}

	// 장바구니에 상품 추가
	public Cart addCart(Long customerId, AddProductCartForm form) {

		Cart cart = redisClient.get(customerId, Cart.class);

		if (cart == null) {
			cart = new Cart();
			cart.setCustomerId(customerId);
		}

		// 이전에 같은 상품이 있는지
		Optional<Cart.Product> productOptional =
				cart.getProducts().stream()
						.filter(product1 -> product1.getId().equals(form.getId()))
						.findFirst();

		if (productOptional.isPresent()) {

			Cart.Product redisProduct = productOptional.get();

			List<Cart.ProductItem> items = form.getItems()
					.stream()
					.map(Cart.ProductItem::from)
					.collect(Collectors.toList());

			Map<Long, Cart.ProductItem> redisItemMap =
					redisProduct.getItems()
							.stream()
							.collect(Collectors.toMap(it -> it.getId(), it -> it));

			//  장바구니의 상품명 != 추가된 상품명
			if (!redisProduct.getName().equals(form.getName())) {
				cart.addMessage(redisProduct.getName() + "의 정보가 변경되었습니다. 확인 부탁드립니다.");
			}

			for (Cart.ProductItem item : items) {
				Cart.ProductItem redisItem = redisItemMap.get(item.getId());

				if (redisItem == null) {
					// happy case
					redisProduct.getItems().add(item);
				} else {
					if (!redisItem.getPrice().equals(item.getPrice())) {
						cart.addMessage(redisItem.getName() + item.getName() + "의 가격이 변경도었습니다.");
					}
					redisItem.setCount(redisItem.getCount() + item.getCount());
				}

			}

		} else {
			Cart.Product prodcut = Cart.Product.from(form);
			cart.getProducts().add(prodcut);
			redisClient.put(customerId, cart);
			return cart;
		}
		redisClient.put(customerId, cart);
		return cart;
	}
}
