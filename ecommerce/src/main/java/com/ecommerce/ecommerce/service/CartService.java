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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
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
        log.info("Fetching cart for user ID: {}", userId);

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(()-> {
                    log.warn("cart not found for user ID: {}", userId);
                    return new ResourceNotFoundException("cart not found for user ID: " + userId);
                });

        log.info("Cart retrieved successfully for user ID: {}. Cart ID: {}",userId, cart.getId());
        CartResponseDTO responseDTO = toCartResponseDto(cart);

        log.info("CartResponseDTO created for user ID: {}", userId);
        return responseDTO;
    }


    public Cart getCartEntityByUserId(long userId){
        log.info("Fetching cart entity for user ID: {}", userId);

        return cartRepository.findByUser_Id(userId)
                .orElseThrow(()-> {
                    return new ResourceNotFoundException("Cart not found for user ID:" + userId);
                });
    }


    public CartResponseDTO toCartResponseDto(Cart cart){
        log.info("Converting Cart (ID: {}) to CartResponseDTO", cart.getId());

        List<CartItemDTO> items = cart.getProduct().stream()
                .map(products -> new CartItemDTO(
                        products.getId(),
                        products.getName(),
                        1,
                        products.getPrice()
                ))
                .toList();

        log.debug("Mapped {} product(s) to CartItemDTO", items.size());

        BigDecimal total = items.stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Total calculated for Cart (ID: {}) : {}", cart.getId(), total);

        return new CartResponseDTO(
                cart.getId(),
                cart.getUser().getId(),
                items,
                total
        );
    }




    public Cart addProductToCart(Long userId, Long productId){
        log.info("Attempting to add product (ID: {}) to cart of user (ID: {})", productId,userId);

        Cart cart= cartRepository.findByUser_Id(userId)
                .orElseGet(()-> {
                    log.info("No existing cart found for user ID: {}. Creating new cart.", userId);

                    Users users= userRepository.findById(userId)
                            .orElseThrow(()-> {
                                        log.error("User not found with ID: {}", userId);
                                        return new ResourceNotFoundException("User not found with ID: " + userId);
                                    });

                    Cart newCart= new Cart();
                    newCart.setUser(users);
                    Cart savedCart = cartRepository.save(newCart);

                    log.info("New cart created for user ID: {}", userId);
                    return savedCart;
                });

        Products products= productsRepository.findById(productId)
                .orElseThrow(()-> {
                         log.error("Product not found with ID: {}", productId);
                         return new ResourceNotFoundException("Product not found with ID: " + productId);
                });

        if (cart.getProduct().contains(products)){
            log.warn("Product ID: {} is already in the cart of user ID: {}", productId, userId);
        } else {
            cart.getProduct().add(products);
            log.info("Product ID: {} added to cart of user ID: {}", productId, userId);
        }

        Cart updatedCart = cartRepository.save(cart);
        log.debug("Cart updated successfully for user ID: {}. Total products: {}",userId,cart.getProduct());
        return updatedCart;
    }




    public Cart removeProductFromCart(Long userId, Long productId){
        log.info("Attempting to remove product (ID: {}) to cart of user (ID:{})", productId, userId);

        Cart cart = getCartEntityByUserId(userId);
        log.debug("Cart retrieved for user ID: {}", userId);

        Products products= productsRepository.findById(productId)
                .orElseThrow(()->{
                    log.error("Product not found with ID: {} ",productId);
                    return new ResourceNotFoundException("Product not found with ID: " + productId);
                });

        boolean removed = cart.getProduct().remove(products);
        if (removed){
            log.info("Product ID: {} removed from cart of user ID: {}", productId, userId);
        }else {
            log.warn("Product ID: {} was not found in the cart of user ID: {}",productId, userId);
        }

        Cart updatedCart = cartRepository.save(cart);
        log.debug("Cart updated successfully for user ID: {}", userId);

        return updatedCart;
    }


}
