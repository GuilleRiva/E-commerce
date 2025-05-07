package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.XResponseDTO.ReviewResponseDTO;
import com.ecommerce.ecommerce.model.Review;
import com.ecommerce.ecommerce.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@Tag(name = "reviews", description = "Endpoints for managing customer reviews in the store")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "List all reviews", description =
    "List all reviews available in the system")
    @PreAuthorize("hasAnyRole('ADMIN, SELLER, CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews(){

        log.info("Request retrieves to fetch all reviews");
        return ResponseEntity.ok(reviewService.getAll());
    }


    @Operation(summary = "Get reviews by ID", description =
    "Retrieves a specified review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Retrieves review successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @GetMapping("/{id}")
    public ResponseEntity<Review>getReviewById(@PathVariable Long id){
        log.info("Request to retrieve review with ID: {}", id);

        return reviewService.getById(id)
                .map(review -> {

                    log.info("Review found with ID: {}", review.getId());
                    return ResponseEntity.ok(review);
                })
                .orElseGet(()-> {

                    log.warn("No review found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }


    @Operation(summary = "create a new review", description =
    "Creates a new review from a user for a specific product. Must include rating and comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid review data"),
            @ApiResponse(responseCode = "404", description = "Product or user not found")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<Review>createReview(@RequestBody Review review){

        log.info("Attempting to create review for product ID:{}, by user ID: {}",
                review.getProduct().getId() , review.getUser().getId());

        Review saved = reviewService.save(review);
        log.info("Review created successfully with ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

}
