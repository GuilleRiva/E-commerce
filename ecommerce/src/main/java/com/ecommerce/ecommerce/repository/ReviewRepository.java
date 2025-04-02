package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review>findByProductId(Long productId);

    Optional<Review>findByUserIdAndProductId(Long userId, Long ProductId);
}
