package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Favorite;
import com.ecommerce.ecommerce.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "favorites", description = "Endpoints to favorite management")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }


    @Operation(summary = "retrieves favorites by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorites retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found or has no favorites")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorite>>getFavoritesByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(favoriteService.getByUserId(userId));
    }



    @Operation(summary = "Check if a product is marked as favorite by a user",
    description = "Returns true if the given product is already marked as favorite by the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully checked favorite existence"),
            @ApiResponse(responseCode = "400", description = "Invalid userId or productID"),
            @ApiResponse(responseCode = "404", description = "User or product not found")
    })
    @GetMapping("/exists")
    public ResponseEntity<Boolean>existsByUserAndProduct(
            @Parameter(description = "ID of the user to check", required = true)
            @RequestParam Long userId,
            @Parameter(description = "ID of the product to check", required = true)
            @RequestParam Long productId
    ){
        boolean exists= favoriteService.existsByUserAndProductId(userId, productId);
        return ResponseEntity.ok(exists);
    }



    @Operation(summary = "Add product to favorites", description =
    "Marks a product as favorite for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "product add as favorite successfully"),
            @ApiResponse(responseCode = "400", description = "couldn't be add as favorite")
    })
    @PostMapping
    public ResponseEntity<Favorite>addFavorite(@RequestBody Favorite favorite){
        return ResponseEntity.ok(favoriteService.addFavorite(favorite));
    }




    @Operation(summary = "remove products from favorite", description =
    "removes a product from the user's list of favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "product remove as favorite successfully"),
            @ApiResponse(responseCode = "404", description = "couldn't be removed the product marked as favorite")
    })
    @DeleteMapping
    public ResponseEntity<Void>removeFavorite(
            @Parameter(description = "ID of the user check", required = true)
            @RequestParam Long userId,
            @Parameter(description = "ID of the product product", required = true)
            @RequestParam Long productId
    ){
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
