package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Favorite;
import com.ecommerce.ecommerce.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public List<Favorite>getByUserId(Long userId){
        return favoriteRepository.findByUser_Id(userId);
    }

    public boolean existsByUserAndProductId(Long userId, Long productId){
        return favoriteRepository.existsByUser_IdAndProduct_Id(userId, productId);
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
