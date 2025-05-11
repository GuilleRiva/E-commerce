package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XResponseDTO.ReviewResponseDTO;
import com.ecommerce.ecommerce.model.Review;
import com.ecommerce.ecommerce.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewResponseDTO> getAll(){
        log.info("Fetching all reviews from the database");

        List<Review> reviews = reviewRepository.findAll();
        log.debug("Total reviews fetched: {}", reviews.size());

        List<ReviewResponseDTO> response = reviews.stream()
                .map(this::toReviewResponseDTO)
                .collect(Collectors.toList())
                .reversed();

        log.info("Returning {} review DTOs in reverse order", response.size());

        return response;
    }


   public ReviewResponseDTO toReviewResponseDTO(Review review){
        log.debug("Mapping Review entity to ReviewResponseDTO for review ID: {}", review.getId());

        return new ReviewResponseDTO(
                review.getId(),
                review.getUser().getUsername(),
                review.getProduct().getName(),
                review.getQualifications(),
                review.getComments(),
                review.getCreatedDate()
        );
    }

    public List<Review>getProductId(Long productId){
        log.info("Fetching product with ID: {}", productId);
        return reviewRepository.findByProductId(productId);
    }

    public Optional<Review>getById(Long id){
        log.info("Fetching by ID: {}", id);
        return reviewRepository.findById(id);
    }

    public Review save(Review review){
        log.info("Attempting to save a new review for product ID: {} by user ID: {}",
                review.getProduct().getId(), review.getUser().getId());

        Review savedReview = reviewRepository.save(review);
        log.info("Review saved successfully with ID: {}", savedReview.getId() );

        return savedReview;
    }

}
