package com.ecommerce.ecommerce.security;

import com.ecommerce.ecommerce.model.RefreshToken;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.RefreshTokenRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

  /*  @Value("${jwt.refresh.expiration}")*/
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken( Long userId){
        Users user = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with id: " + userId));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken.setExpiry_date(Instant.now().plusMillis(refreshTokenDurationMs));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyToken (RefreshToken token){
        if (token.getExpiry_date().isBefore(Instant.now())){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Runtime token has expired. Please sign in again.");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByUserId (Long userId){
        refreshTokenRepository.deleteByUserId(userId);
    }
}
