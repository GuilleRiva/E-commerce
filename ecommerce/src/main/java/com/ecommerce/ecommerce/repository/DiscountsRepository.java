package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Discounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountsRepository extends JpaRepository<Discounts, Long> {

        List<Discounts>findByProductId(Long productId);

        List<Discounts>findByEndDateBeforeAndEndDateAfter(LocalDateTime dateInit, LocalDateTime endDate);

        Optional<Discounts> findByProductIdAndStartDateBeforeAndEndDateAfter(Long productId, LocalDateTime dateInit, LocalDateTime endDate);
}
