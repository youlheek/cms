package com.zerobase.cms.order.domain.product;

import lombok.Getter;

@Getter
public class UpdateProductItemForm {
	private Long productId;
	private Long itemId;
	private String name;
	private Integer price;
	private Integer count;
}
