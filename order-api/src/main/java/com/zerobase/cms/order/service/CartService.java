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

	private final RedisClient redisClient;

	// 장바구니 가져오기
	public Cart getCart(Long customerId) {
		Cart cart = redisClient.get(customerId, Cart.class);
		return cart != null ? cart : new Cart(customerId);
	}

	public Cart putCart(Long customerId, Cart cart) {
		redisClient.put(customerId, cart);
		return cart;
	}

	// 장바구니에 상품 추가
	public Cart addCart(Long customerId, AddProductCartForm form) {

		Cart cart = redisClient.get(customerId, Cart.class);

		if (cart == null) {
			cart = new Cart();
			cart.setCustomerId(customerId);
		}

		// 카트에 추가하려는 상품이 있는지
		Optional<Cart.Product> productOptional =
				cart.getProducts().stream()
						.filter(product1 -> product1.getId().equals(form.getId()))
						.findFirst();

		// 카트에 이미 있는 상품일때 -> 옵션 수량 추가
		if (productOptional.isPresent()) {

			// 장바구니에 이미 존재하는 해당 상품 가져오기
			Cart.Product redisProduct = productOptional.get();

			// 추가할 옵션 List 생성
			List<Cart.ProductItem> items = form.getItems()
					.stream()
					.map(Cart.ProductItem::from)
					.collect(Collectors.toList());

			// 장바구니 상품 옵션을 맵<id, items> 로 만듬
			Map<Long, Cart.ProductItem> redisItemMap =
					redisProduct.getItems()
							.stream()
							.collect(Collectors.toMap(it -> it.getId(), it -> it));

			// 장바구니의 상품명 != 추가된 상품명
			if (!redisProduct.getName().equals(form.getName())) {
				cart.addMessage(redisProduct.getName() + "의 정보가 변경되었습니다. 확인 부탁드립니다.");
			}

			// 추가할 옵션 리스트들을 순회하며 item에 담기
			for (Cart.ProductItem formItem : items) {
				// 추가할 옵션이 카트에 있으면 get
				Cart.ProductItem redisItem = redisItemMap.get(formItem.getId());

				if (redisItem == null) {
					// happy case -> 걍 집어넣기만 하면 된다!
					redisProduct.getItems().add(formItem);
				} else {
					if (!redisItem.getPrice().equals(formItem.getPrice())) {
						cart.addMessage(redisProduct.getName() + " 상품의 옵션 " +redisItem.getName() + " 의 가격이 변경되었습니다. 확인 부탁드립니다.");
					}
					// TODO : 옵션명 변경됐을때도 분기해야할듯?
					redisItem.setCount(redisItem.getCount() + formItem.getCount());
				}

			}

			// TODO : cart.addMessage 하고 나서 다음 실행시 message clear된 채로 나와야하지않을까?

		} else { // 카트에 없는 상품일 때
			Cart.Product product = Cart.Product.from(form);
			cart.getProducts().add(product);
		}
		redisClient.put(customerId, cart);
		return cart;
	}
}
