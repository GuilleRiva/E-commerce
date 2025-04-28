package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XResponseDTO.ReviewResponseDTO;
import com.ecommerce.ecommerce.model.Review;
import com.ecommerce.ecommerce.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewResponseDTO> getAll(){
        return reviewRepository.findAll().stream()
                .map(this::toReviewResponseDTO)
                .collect(Collectors.toList()).reversed();
    }


   public ReviewResponseDTO toReviewResponseDTO(Review review){
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
        return reviewRepository.findByProductId(productId);
    }

    public Optional<Review>getById(Long id){
        return reviewRepository.findById(id);
    }

    public Review save(Review review){
        return reviewRepository.save(review);
    }

}
