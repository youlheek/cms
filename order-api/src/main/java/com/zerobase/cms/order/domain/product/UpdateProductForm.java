package com.zerobase.cms.order.domain.product;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateProductForm {
	private Long productId;
	private String name;
	private String description;
	private List<UpdateProductItemForm> itmes;

}
