package com.zerobase.cms.user.domain.model;

import com.zerobase.cms.user.domain.SignUpForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Seller extends BaseEntity {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스가 임의로 키값 증가
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;
	private String name;
	private String password;
	private String phone;
	private LocalDate birth;

	private LocalDateTime verifyExpiredAt;
	private String verificationCode;
	private boolean verify;

	//
	private Integer balance;

	public static Seller from (SignUpForm form) {
		return Seller.builder()
				.email(form.getEmail())
				.password(form.getPassword())
				.name(form.getName())
				.phone(form.getPhone())
				.birth(form.getBirth())
				.verify(false)
				.build();
	}
}
