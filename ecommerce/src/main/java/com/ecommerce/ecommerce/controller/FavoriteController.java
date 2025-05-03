package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.XResponseDTO.FavoriteResponseDTO;
import com.ecommerce.ecommerce.model.Favorite;
import com.ecommerce.ecommerce.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @PreAuthorize("hasAnyRole('CUSTOMER, ADMIN, SELLER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteResponseDTO>>getFavoritesByUserId(@PathVariable Long userId){
        List<FavoriteResponseDTO> favorites = favoriteService.getByUserId(userId).stream()
                .map(favoriteService::toFavoriteResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(favorites);
    }



    @Operation(summary = "Check if a product is marked as favorite by a user",
    description = "Returns true if the given product is already marked as favorite by the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully checked favorite existence"),
            @ApiResponse(responseCode = "400", description = "Invalid userId or productID"),
            @ApiResponse(responseCode = "404", description = "User or product not found")
    })
    @PreAuthorize("hasAnyRole('CUSTOMER, ADMIN, SELLER')")
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
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<FavoriteResponseDTO>addFavorite(@Valid @RequestBody FavoriteResponseDTO dto){
        Favorite favorite = favoriteService.toFavoriteEntity(dto);
        Favorite saved = favoriteService.addFavorite(favorite);
        FavoriteResponseDTO responseDTO = favoriteService.toFavoriteResponseDTO(saved);
        return ResponseEntity.ok(responseDTO);
    }




    @Operation(summary = "remove products from favorite", description =
    "removes a product from the user's list of favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "product remove as favorite successfully"),
            @ApiResponse(responseCode = "404", description = "couldn't be removed the product marked as favorite")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
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
