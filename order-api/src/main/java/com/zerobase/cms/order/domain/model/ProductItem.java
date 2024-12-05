package com.zerobase.cms.order.domain.model;

import com.zerobase.cms.order.domain.product.AddProductItemForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class ProductItem extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long sellerId;

	@Audited
	private String name;

	@Audited
	private Integer price; // 가격

	private Integer count; // 재고

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private Product product;


	// 파라미터가 하나인경우 -> from
	// 파라미터가 두개 이상인 경우 -> of
	public static ProductItem of(Long sellerId, AddProductItemForm form) {
		return ProductItem.builder()
				.sellerId(sellerId)
				.name(form.getName())
				.price(form.getPrice())
				.count(form.getCount())
				.build();
	}
}
