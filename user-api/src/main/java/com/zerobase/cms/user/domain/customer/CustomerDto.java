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
	private Integer balance;

	public static CustomerDto from (Customer customer) {
		return new CustomerDto(customer.getId(), customer.getEmail(), customer.getBalance() == null?0:customer.getBalance());
		// TODO : 📍 balance 초기값 생성을 0으로 할 순 없나?
	}
}

// 📍 왜 build 안쓰고 new 로 쓰지?
/*
* 빌더 패턴은 주로 복잡한 객체를 생성할 때 사용합니다.
* 많은 필드가 있거나, 필수와 선택 필드가 구분되어 있는 경우 빌더 패턴을 사용하면 코드의 가독성과 유지보수성이 높아집니다.
* 하지만 CustomerDto 클래스는 필드가 단 2개(id와 email)밖에 없어서, 빌더 패턴을 사용하지 않고도 간단히 생성할 수 있습니다.
* 이 경우 new를 사용하여 직접 생성하는 것이 더 간단하고 명료합니다.

* 빌더 패턴의 장점은 유연한 객체 생성과 가독성에 있는데,
* 필드가 적고 생성 로직이 단순한 경우에는 굳이 빌더 패턴을 사용할 필요가 없습니다.

* CustomerDto의 목적은 단순히 데이터 전송이므로,
* 복잡한 생성 방식을 사용하지 않고 간단히 객체를 생성하기 위해 new 키워드를 사용하는 것이 더 일반적입니다.
* */