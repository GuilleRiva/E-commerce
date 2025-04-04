package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

    List<Favorite>findByUserId(Long userId);

    boolean existsByUserAndProduct(Long userId, Long productId);

    Optional<Favorite>findByUserIdAndProductId(Long userId,Long productId);
}
