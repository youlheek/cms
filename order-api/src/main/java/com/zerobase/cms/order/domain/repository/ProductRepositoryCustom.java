package com.zerobase.cms.order.domain.repository;

import com.zerobase.cms.order.domain.model.Product;

import java.util.List;

public interface ProductRepositoryCustom {
	List<Product> searchByName(String name);
}
