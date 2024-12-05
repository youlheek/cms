package com.zerobase.cms.order.domain.repository;

import com.zerobase.cms.order.domain.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

	// TODO : 어노테이션 체크
	// 블로그에서는 해당 엔티티에 @ManyToOne(fetch=FetchType.Lazy) 에서 Eager로 변경해주기도 하던데,,,,
	@EntityGraph(attributePaths = {"productItems"}, type = EntityGraph.EntityGraphType.LOAD)
	Optional<Product> findWithProductItemsById(Long id);

	Optional<Product> findBySellerIdAndId(Long sellerId, Long id);
}
