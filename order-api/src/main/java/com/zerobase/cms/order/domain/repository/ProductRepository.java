package com.zerobase.cms.order.domain.repository;

import com.zerobase.cms.order.domain.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

	// 블로그에서는 해당 엔티티에 @ManyToOne(fetch=FetchType.Lazy) 에서 Eager로 변경해주기도 하던데,,,,
	@EntityGraph(attributePaths = {"productItems"}, type = EntityGraph.EntityGraphType.LOAD)
	Optional<Product> findWithProductItemsById(Long id);
	// Product라는 상품 정보를 id 로 찾으면서 연관된 productItems도 같이 가져옴
	// Lazy : 보통 JPA는 연결된 데이터를 필요할때만 가져오려고 함
	// Load : 한번에 다 가져오겠다

	@EntityGraph(attributePaths = {"productItems"}, type = EntityGraph.EntityGraphType.LOAD)
	Optional<Product> findBySellerIdAndId(Long sellerId, Long id);

	@EntityGraph(attributePaths = {"productItems"}, type = EntityGraph.EntityGraphType.LOAD)
	List<Product> findAllByIdIn(List<Long> ids);
}
