package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Favorite;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository {

    List<Favorite>findByUserId(Long userId);

    Optional<Favorite>findByUserIdAndProductId(Long userId,Long productId);
}
