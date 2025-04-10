package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review>findByProductId(Long productId);

    List<Review>findByUser_Id(Long userId);

    Optional<Review>findByUser_IdAndProduct_Id(Long userId, Long ProductId);
}
