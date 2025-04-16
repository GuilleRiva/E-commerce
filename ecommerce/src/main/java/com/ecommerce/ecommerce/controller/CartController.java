package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.XResponseDTO.CartResponseDTO;
import com.ecommerce.ecommerce.model.Cart;
import com.ecommerce.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "cart", description = "endpoints for cart management")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }



    @Operation(summary = "Get by user", description =
    "Get user's shopping cart by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User cart found"),
            @ApiResponse(responseCode = "404", description = "user cart not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDTO> getCartByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }



    @Operation(summary = "add product to cart", description =
    "add product to cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "add product to cart successfully"),
            @ApiResponse(responseCode = "404", description = "the product couldn't no be add to the cart")
    })
    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart>addProductToCart(
            @PathVariable Long userId,
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(cartService.addProductToCart(userId, productId));
    }



    @Operation(summary = "remove product", description = "remove product from cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "product successfully removed from cart "),
            @ApiResponse(responseCode = "404",description = "the product couldn't be removed to the cart")
    })
    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Cart>removeProductFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(cartService.removeProductFromCart(userId, productId));
    }
}
