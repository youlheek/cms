package com.zerobase.cms.user.repository;

import com.zerobase.cms.user.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>{
	Optional<Customer> findByEmail(String email);
}
