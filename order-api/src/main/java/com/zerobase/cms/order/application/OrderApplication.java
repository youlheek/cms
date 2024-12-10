package com.zerobase.cms.order.application;

import com.zerobase.cms.order.client.UserClient;
import com.zerobase.cms.order.client.customer.ChangeBalanceForm;
import com.zerobase.cms.order.client.customer.CustomerDto;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.service.ProductItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

import static com.zerobase.cms.order.exception.ErrorCode.ORDER_FAIL_CART_CHECK;
import static com.zerobase.cms.order.exception.ErrorCode.ORDER_FAIL_ENOUGH_MONEY;

@Service
@RequiredArgsConstructor
public class OrderApplication {
	/**
	 * 결제를 위해 필요한 것
	 * 1. 물건들이 전부 주문 가능한 상태인지 확인
	 * 2. 가격 변동이 있었는지 확인
	 * 3. 고객의 돈이 충분한지 확인
	 * 4. 결제 + 상품의 재고 관리
	 */

	private final CartApplication cartApplication;
	private final UserClient userClient;
	private final ProductItemService productItemService;


	@Transactional
	// TODO : 지금 주문하는 동안 다른데서 계좌 잔고를 변경했을 경우 rollback 전략을 수립해야 한다
	public void order(String token, Cart cart) {
		// (현재 전략) 주문 시 기존 카트 버림
		// (TODO) 선택주문 : 내가 사지 않은 아이템을 살려야 함
		Cart orderCart = cartApplication.refreshCart(cart);
//		Cart orderCart = cartApplication.getCart(customerId);

		// 1, 2번 message의 유무로 확인 끝
		if (orderCart.getMessages().size() > 0) {
			// 문제가 있음
			throw new CustomException(ORDER_FAIL_CART_CHECK);
		}

		// 3. 고객의 돈이 충분한지 확인
		CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();
		Integer totalPrice = getTotalPrice(orderCart);
		if (customerDto.getBalance() < totalPrice) {
			throw new CustomException(ORDER_FAIL_ENOUGH_MONEY);
		}

		// 4-1. 주문 결제
		// 롤백 계획에 대해서 생각해야 함
		userClient.changeBalance(token, ChangeBalanceForm.builder()
				.from("USER")
				.message("Order")
				.money(-totalPrice)
				.build());

		// 4-2. 상품의 재고관리 (DB)
		for (Cart.Product cartProduct : orderCart.getProducts()) {
			for (Cart.ProductItem cartItem : cartProduct.getItems()) {
				// DB에서 상품 가져오기
				ProductItem dbItem = productItemService.getProductItem(cartItem.getId());
				dbItem.setCount(dbItem.getCount() - cartItem.getCount());
			}
		}
	}

	private Integer getTotalPrice(Cart cart) {

		return cart.getProducts().stream().flatMapToInt(
						product -> product.getItems().stream().flatMapToInt(
								productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())
						))
				.sum();
	}

}
