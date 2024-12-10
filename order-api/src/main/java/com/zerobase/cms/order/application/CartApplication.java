package com.zerobase.cms.order.application;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.service.CartService;
import com.zerobase.cms.order.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zerobase.cms.order.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CartApplication {
	private final ProductSearchService productSearchService;
	private final CartService cartService;

	/**
	 * 현재 product & productItem id 값으로만 추가되고 있음
	 * 추가될 때 DB 상품명 & 상품 옵션명과 장바구니의 상품명 & 상품옵션명 확인해서
	 * TODO : add.Message or 상품명 변경 필요할듯
	 */

	// 장바구니 상품 추가
	public Cart addCart(Long customerId, AddProductCartForm form) {

		Product product = productSearchService.getByProductId(form.getId());
		if (product == null) {
			throw new CustomException(NOT_FOUND_PRODUCT);
		}

		Cart cart = cartService.getCart(customerId);
		if (!addAble(cart, product, form)) {
			throw new CustomException(ITEM_COUNT_NOT_ENOUGH);
		}

		return cartService.addCart(customerId, form);
	}

	// 추가할 상품의 수량이 충분한지 검증
	private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
		// 카트에 넣으려는 상품이 카트에 이미 존재하는 상품인지 확인하고
		// 있는 것이면 cart.Product 객체 생성해서 반환
		Cart.Product cartProduct = cart.getProducts().stream()
				.filter(p -> p.getId().equals(form.getId()))
				.findFirst()
				.orElse(Cart.Product.builder()
						.id(product.getId())
						.items(Collections.emptyList())
						.build()); // 없으면 cart.Product 빌드해서 반환

		// 내 장바구니에 있는 품목의 옵션별 수량을 map으로 변환시켜서 cartItemCountMap으로 저장
		Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
				.collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));

		// DB에 있는 실제 품목 옵션별 수량을 map으로 변환시켜서 dbItemCountMap으로 저장
		Map<Long, Integer> dbItemCountMap = product.getProductItems().stream()
				.collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));

		// DB 수량 > 장바구니 + form 수량
		return form.getItems().stream().noneMatch(
				// noneMatch : 스트림의 모든 요소가 조건을 만족하지 않아야 true를 반환
				formItem -> {
					// 장바구니에 이미 추가된 해당 옵션의 수량
					Integer cartCount = cartItemCountMap.getOrDefault(formItem.getId(), 0);

					// 해당 옵션의 DB 재고 수량
					Integer dbCount = dbItemCountMap.get(formItem.getId());
					if (dbCount == null) {
						throw new CustomException(NOT_FOUND_ITEM);
					}

					return formItem.getCount() + cartCount > dbCount;
				});
	}

	// 장바구니 가져오기
	// 1. 장바구니에 상품을 추가했다
	// 2. 상품의 가격이나 수량이 변동됐다
	public Cart getCart(Long customerId) {
		Cart cart = refreshCart(cartService.getCart(customerId));
//		cartService.putCart(cart.getCustomerId(), cart);

		Cart returnCart = new Cart();
		returnCart.setCustomerId(customerId);
		returnCart.setProducts(cart.getProducts());
		returnCart.setMessages(cart.getMessages());
		cart.setMessages(new ArrayList<>());
		// 메시지 없는 것
		cartService.putCart(customerId, cart);

		return returnCart;

		// 메시지를 보고 난 다음에는, 이미 본 메시지는 스팸이 되기 때문에 제거한다.
	}

	// 카트 비우기
	public void cleartCart(Long customerId) {
		cartService.putCart(customerId, null);
	}

	// 가격, 수량을 DB와 비교하여 Cart 새로고침
	protected Cart refreshCart(Cart cart) {
		// 1. 상품이나 상품의 아이템의 정보, 가격, 수량이 변경되었는지 체크하고
		// 그에 맞는 알람을 제공해야함
		// 2. 상품의 수량, 가격을 우리가 임의로 변경한다

		// cart의 productId로 DB의 product를 가져와 productMap을 만듬
		Map<Long, Product> productMap =
				productSearchService.getListByProducIds(
								cart.getProducts().stream().map(Cart.Product::getId)
										.collect(Collectors.toList()))
						.stream()
						.collect(Collectors.toMap(Product::getId, product -> product));

		// 카트에 있는 상품들이 실제 존재하는지 확인하고
		// 없으면 카트에서 제거
		for (int i = 0; i < cart.getProducts().size(); i++) {

			Cart.Product cartProduct = cart.getProducts().get(i);

			Product p = productMap.get(cartProduct.getId());

			if (p == null) { // 존재하지 않는 상품일 경우 카트에서 제거
				cart.getProducts().remove(cartProduct);
				i--;
				cart.addMessage(cartProduct.getName() + "상품이 삭제되었습니다.");
				continue;
			}

			Map<Long, ProductItem> productItemMap = p.getProductItems().stream()
					.collect(Collectors.toMap(ProductItem::getId, productItem -> productItem));

			// TODO : 추후에 각각 케이스별로 에러를 분기 & 에러가 정상 출력되는지 테스트
			// ERROR 케이스들
			List<String> tmpMessages = new ArrayList<>();
			for (int j = 0; j < cartProduct.getItems().size(); j++) {

				// cartProductItem 은 참조객체타입이라 set 하면 cart에 반영됨
				Cart.ProductItem cartProductItem = cartProduct.getItems().get(j);
				ProductItem pi = productItemMap.get(cartProductItem.getId());

				if (pi == null) {
					cartProduct.getItems().remove(cartProductItem);
					j--;
					tmpMessages.add(cartProductItem.getName() + " 옵션이 삭제되었습니다.");
					continue;
				}

				boolean isPriceChanged = false, isCountNotEnough = false;
				// 왜 굳이 변수를 사용하느냐? -> 에러가 둘 다 해당될 경우

				if (!cartProductItem.getPrice().equals(pi.getPrice())) {
					isPriceChanged = true;
					cartProductItem.setPrice(pi.getPrice()); // 가격이 변동되었을 때
				}
				if (cartProductItem.getCount() > pi.getCount()) {
					isCountNotEnough = true;
					cartProductItem.setCount(pi.getCount()); // 수량이 변동되었을 때
				}

				if (isPriceChanged && isCountNotEnough) {
					// message 1
					tmpMessages.add(cartProductItem.getName() + " 가격변동, 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
				} else if (isPriceChanged) {
					// message 2
					tmpMessages.add(cartProductItem.getName() + " 가격이 변동되었습니다.");
				} else if (isCountNotEnough) {
					// message 3
					tmpMessages.add(cartProductItem.getName() + " 수량이 부족하여 구매 가능한 최대치로 변경되었습니다.");
				}
			}

			if (cartProduct.getItems().size() == 0) { // 카트 O but 상품 X
				cart.getProducts().remove(cartProduct);
				i--;
				cart.addMessage(cartProduct.getName() + " 상품의 옵션이 없어 구매가 불가능합니다.");
				continue;
			} else if (tmpMessages.size() > 0) { // 에러가 있는 경우
				StringBuilder builder = new StringBuilder();
				builder.append(cartProduct.getName() + " 상품의 변동 사항 : ");

				for (String message : tmpMessages) {
					builder.append(message);
					builder.append("\n");
				}
				cart.addMessage(builder.toString());
			}

		}

		return cart;
	}

	/**
	 * 엣지 케이스
	 * @param customerId
	 * @param cart
	 * @return
	 */
	public Cart updateCart(Long customerId, Cart cart) { // TODO : refreshCart가 있는데 왜 필요한가?
		// 실질적으로 변하는 데이터
		// -> 상품의 삭제, 수량 변경
		cartService.putCart(customerId, cart);
		return getCart(customerId);
	}
}
