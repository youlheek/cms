package com.zerobase.cms.user.domain.customer;

import lombok.Getter;

@Getter
public class ChangeBalanceForm {
	private String from; // 입/출금자
	private String message;
	private Integer money;
}
