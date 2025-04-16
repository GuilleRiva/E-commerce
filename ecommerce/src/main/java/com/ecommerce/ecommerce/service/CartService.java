package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XResponseDTO.CartItemDTO;
import com.ecommerce.ecommerce.dto.XResponseDTO.CartResponseDTO;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Cart;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.CartRepository;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductsRepository productsRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productsRepository = productsRepository;
        this.userRepository = userRepository;
    }



    public  CartResponseDTO getCartByUserId(Long userId){
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(()-> new ResourceNotFoundException("cart not found for user ID: " + userId));

        return toCartResponseDto(cart);
    }




    public Cart getCartEntityByUserId(Long userId){
        return cartRepository.findByUser_Id(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found for user ID: " + userId));
    }




    public CartResponseDTO toCartResponseDto(Cart cart){
        List<CartItemDTO> items = cart.getProduct().stream()
                .map(products -> new CartItemDTO(
                        products.getId(),
                        products.getName(),
                        1,
                        products.getPrice()
                ))
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(
                cart.getId(),
                cart.getUser().getId(),
                items,
                total
        );
    }




    public Cart addProductToCart(Long userId, Long productId){
        Cart cart= cartRepository.findByUser_Id(userId)
                .orElseGet(()-> {
                    Users users= userRepository.findById(userId)
                            .orElseThrow(()-> new ResourceNotFoundException("User not found with ID: " + userId));
                    Cart newCart= new Cart();
                    newCart.setUser(users);
                    return cartRepository.save(newCart);
                });

        Products products= productsRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with ID: " + productId));
        cart.getProduct().add(products);
        return cartRepository.save(cart);
    }




    public Cart removeProductFromCart(Long userId, Long productId){
        Cart cart = getCartEntityByUserId(userId);
        Products products= productsRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with ID: " + productId));
        cart.getProduct().remove(products);
        return cartRepository.save(cart);
    }
}
