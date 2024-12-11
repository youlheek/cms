package com.zerobase.cms.order.client.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor @AllArgsConstructor
public class ChangeBalanceForm {

	private String from; // 입/출금자
	private String message;
	private Integer money;
}
