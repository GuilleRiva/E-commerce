package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Products, Long> {
}
