package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {

    List<Products>findByCategoryId(Long categoryId);

    List<Products>findByPriceBetween(BigDecimal min, BigDecimal max);

}
