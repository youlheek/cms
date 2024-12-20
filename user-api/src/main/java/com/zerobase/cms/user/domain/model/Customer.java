package com.zerobase.cms.user.domain.model;

import com.zerobase.cms.user.domain.SignUpForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class) // spring-data-envers 를 implementation 해줘야 쓸 수 있음
// Customer라는 테이블이 업데이트 될 때마다 BaseEntity의 createdDate와 updatedDate가 업데이트 됨
public class Customer extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;
	private String name;
	private String password; // 🏷️실제로는 암호화해야함
	private String phone; // TODO : 🏷️폰번호의 validation을 어떻게 정의할건지
	private LocalDate birth;

	// 이메일 인증을 위한 컬럼
	private LocalDateTime verifyExpiredAt;
	private String verificationCode;
	private boolean verify;

	@Column(columnDefinition = "int default 0")
	private Integer balance;


	public static Customer from (SignUpForm form) {
		return Customer.builder()
				.email(form.getEmail().toLowerCase(Locale.ROOT))
				.password(form.getPassword())
				.name(form.getName())
				.phone(form.getPhone())
				.birth(form.getBirth())
				.verify(false) // verify가 되어있지 않으면 일단은 로그인 X
				.build();
	}
}
