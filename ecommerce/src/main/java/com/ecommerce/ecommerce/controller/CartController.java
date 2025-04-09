package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Cart;
import com.ecommerce.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart>getCartByUserId(@PathVariable Long id){
        return ResponseEntity.ok(cartService.getCartByUserId(id));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart>addProductToCart(
            @PathVariable Long userId,
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(cartService.addProductToCart(userId, productId));
    }

    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Cart>removeProductFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(cartService.removeProductFromCart(userId, productId));
    }
}
