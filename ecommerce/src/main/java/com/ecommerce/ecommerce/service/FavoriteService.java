package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XResponseDTO.FavoriteResponseDTO;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Favorite;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.FavoriteRepository;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, ProductsRepository productsRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.productsRepository = productsRepository;
    }

    public List<Favorite>getByUserId(Long userId){
        return favoriteRepository.findByUser_Id(userId);
    }

    public boolean existsByUserAndProductId(Long userId, Long productId){
        return favoriteRepository.existsByUser_IdAndProduct_Id(userId, productId);
    }

    public Favorite toFavoriteEntity(FavoriteResponseDTO dto){
        Users users = userRepository.findById(dto.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found with ID" + dto.getUserId()));

        Products products = productsRepository.findById(dto.getProductId())
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with ID" + dto.getProductId()));

        Favorite favorite = new Favorite();
        favorite.setUser(users);
        favorite.setProduct(products);
        return favorite;
    }

    public FavoriteResponseDTO toFavoriteResponseDTO (Favorite favorite){
        return new FavoriteResponseDTO(
                favorite.getId(),
                favorite.getProduct().getId(),
                favorite.getProduct().getName(),
                favorite.getUser().getId()
        );
    }

    public Favorite addFavorite( Favorite favorite){
        if (existsByUserAndProductId(
                favorite.getUser().getId(),
                favorite.getProduct().getId())){
            throw new RuntimeException("This product is already marked as a favorite");
        }
        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long userId, Long productId){
        Optional<Favorite>favorite=favoriteRepository.findByUser_IdAndProduct_Id(userId, productId);
        favorite.ifPresent(favoriteRepository::delete);
    }

}
