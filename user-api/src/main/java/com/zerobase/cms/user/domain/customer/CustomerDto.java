package com.zerobase.cms.user.domain.customer;

import com.zerobase.cms.user.domain.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CustomerDto {

	private Long id;
	private String email;

	public static CustomerDto from (Customer customer) {
		return new CustomerDto(customer.getId(), customer.getEmail());
		// 📍 왜 build 안쓰고 new 로 쓰지?
	}
}
