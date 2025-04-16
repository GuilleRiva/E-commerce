package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.XResponseDTO.ReviewResponseDTO;
import com.ecommerce.ecommerce.model.Review;
import com.ecommerce.ecommerce.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "reviews", description = "Endpoints for managing customer reviews in the store")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "List all reviews", description =
    "List all reviews available in the system")
    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews(){
        return ResponseEntity.ok(reviewService.getAll());
    }


    @Operation(summary = "Get reviews by ID", description =
    "Retrieves a specified review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Retrieves review successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Review>getReviewById(@PathVariable Long id){
        return reviewService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "create a new review", description =
    "Creates a new review from a user for a specific product. Must include rating and comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid review data"),
            @ApiResponse(responseCode = "404", description = "Product or user not found")
    })
    @PostMapping
    public ResponseEntity<Review>createReview(@RequestBody Review review){
        return ResponseEntity.ok(reviewService.save(review));
    }

}
