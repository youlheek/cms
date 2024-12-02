package com.zerobase.cms.user.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class CustomerBalanceHistory extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(targetEntity = Customer.class, fetch = FetchType.LAZY)
	private Customer customer;

	// 변경 금액 (입출금액)
	private Integer changeMoney;
	private Integer currentMoney;

	private String fromMessage; // from으로 할 경우에는 쿼리취급당할수있음
	private String description;

}
