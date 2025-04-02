package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Discounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountsRepository extends JpaRepository<Discounts, Long> {

        List<Discounts>findByProductId(Long productId);

}
