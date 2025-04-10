package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Cart;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.CartRepository;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Cart getCartByUserId(Long userId){
        return cartRepository.findByUser_Id(userId)
                .orElseThrow(()-> new ResourceNotFoundException("cart not found for user ID: " + userId));
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
        Cart cart= getCartByUserId(userId);
        Products products= productsRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with ID: " + productId));
        cart.getProduct().remove(products);
        return cartRepository.save(cart);
    }
}
