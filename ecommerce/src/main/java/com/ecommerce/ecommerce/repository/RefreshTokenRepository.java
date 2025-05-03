package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken (String token);

    void deleteByUserId(Long userId);
}
