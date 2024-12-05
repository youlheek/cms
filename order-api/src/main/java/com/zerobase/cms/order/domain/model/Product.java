package com.zerobase.cms.order.domain.model;

import com.zerobase.cms.order.domain.product.AddProductForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Audited // 이 엔티티의 데이터가 변할때마다 그 내용을 저장함 / 상품 옵션이 변경될 때 트래킹이 가능
// 실무에서는 잘 쓰이지 않는 편
@AuditOverride(forClass = BaseEntity.class)
public class Product extends BaseEntity {
// 상품 옵션

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long sellerId;

	private String name;

	private String description; // 상세옵션 - 일단 간단하게!

	@OneToMany(cascade = CascadeType.ALL) // Cascade : 부모 엔티티의 작업이 자식 엔티티에도 자동으로 전파됨
	@JoinColumn(name = "product_id") // product_id 라는 외래키를 만들어서 조인
	private List<ProductItem> productItems = new ArrayList<>();

	// 변환작업
	public static Product of(Long sellerId, AddProductForm form) {
		return Product.builder()
				.sellerId(sellerId)
				.name(form.getName())
				.description(form.getDescription())
				.productItems(form.getItmes()
						.stream()
						.map(piForm -> ProductItem.of(sellerId, piForm)).collect(Collectors.toList()))
				.build();
	}
}