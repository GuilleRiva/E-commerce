package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Review;
import com.ecommerce.ecommerce.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review>getAll(){
        return reviewRepository.findAll();
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
