package com.zerobase.cms.user.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class) // spring-data-envers 를 implementation 해줘야 쓸 수 있음
// Customer라는 테이블이 업데이트 될 때마다 BaseEntity의 createdDate와 updatedDate가 업데이트 됨
public class Customer extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;
	private String name;
	private String password;
	private LocalDate birth;
}
