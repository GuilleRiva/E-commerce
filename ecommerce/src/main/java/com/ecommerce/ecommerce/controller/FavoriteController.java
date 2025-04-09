package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Favorite;
import com.ecommerce.ecommerce.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorite>>getFavoritesByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(favoriteService.getByUserId(userId));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean>existsByUserAndProduct(
            @RequestParam Long userId,
            @RequestParam Long productId
    ){
        boolean exists= favoriteService.existsByUserAndProductId(userId, productId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping
    public ResponseEntity<Favorite>addFavorite(@RequestBody Favorite favorite){
        return ResponseEntity.ok(favoriteService.addFavorite(favorite));
    }

    @DeleteMapping
    public ResponseEntity<Void>removeFavorite(
            @RequestParam Long userId,
            @RequestParam Long productId
    ){
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
