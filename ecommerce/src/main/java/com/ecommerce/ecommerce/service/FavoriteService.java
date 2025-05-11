package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XResponseDTO.FavoriteResponseDTO;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Favorite;
import com.ecommerce.ecommerce.model.Products;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.FavoriteRepository;
import com.ecommerce.ecommerce.repository.ProductsRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        log.info("Fetching favorite  with user  ID: {}",userId);
        return favoriteRepository.findByUser_Id(userId);
    }

    public boolean existsByUserAndProductId(Long userId, Long productId){

        log.info("Checking if product ID: {} is marked as favorite by user ID: {}", productId, userId);
        return favoriteRepository.existsByUser_IdAndProduct_Id(userId, productId);
    }


    public Favorite toFavoriteEntity(FavoriteResponseDTO dto){
        log.debug("Mapping FavoriteResponseDTO to favorite entity: {}", dto);

        Users users = userRepository.findById(dto.getUserId())
                .orElseThrow(()-> {
                    log.error("User not found with ID: {}", dto.getUserId());
                        return new ResourceNotFoundException("User not found with ID" + dto.getUserId());
                });


        Products products = productsRepository.findById(dto.getProductId())
                .orElseThrow(()-> {
                    log.error("Product not found with ID: {}", dto.getProductId());
                           return new ResourceNotFoundException("Product not found with ID" + dto.getProductId());
                });


        Favorite favorite = new Favorite();
        favorite.setUser(users);
        favorite.setProduct(products);
        return favorite;
    }


    public FavoriteResponseDTO toFavoriteResponseDTO (Favorite favorite){
        log.debug("Mapping Favorite entity to FavoriteResponseDTO: {}", favorite);

        return new FavoriteResponseDTO(
                favorite.getId(),
                favorite.getProduct().getId(),
                favorite.getProduct().getName(),
                favorite.getUser().getId()
        );
    }


    public Favorite addFavorite( Favorite favorite){

        Long userId = favorite.getUser().getId();
        Long productId = favorite.getProduct().getId();
        log.info("Attempting to add product ID {} as favorite for user ID {}", productId, userId);

        if (existsByUserAndProductId(userId, productId)){
            log.warn("Product ID {} is already marked as favorite for user ID {} ", productId, userId);
            throw new RuntimeException("This product is already marked as a favorite");
        }
        Favorite saved = favoriteRepository.save(favorite);
        log.info("Favorite successfully saved: ID {}", saved.getId());
        return saved;
    }



    public void removeFavorite(Long userId, Long productId){
        log.info("Attempting to remove favorite: user ID {}, product ID {}", userId, productId);

        Optional<Favorite>favorite=favoriteRepository.findByUser_IdAndProduct_Id(userId, productId);
        if (favorite.isPresent()){
            favoriteRepository.delete(favorite.get());
            log.info("Favorite successfully removed for user ID {}, product ID {}", userId,productId);

        }else {
            log.warn("No favorite found to remove for user ID {}, product ID {}", userId, productId);
        }

    }

}
