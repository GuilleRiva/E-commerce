package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

    List<Favorite>findByUser_Id(Long userId);

    boolean existsByUser_IdAndProduct_Id(Long userId, Long productId);

    Optional<Favorite>findByUser_IdAndProduct_Id(Long userId,Long productId);
}
