package com.ecommerce.ecommerce.security;

import com.ecommerce.ecommerce.model.RefreshToken;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.RefreshTokenRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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

    public RefreshToken createRefreshToken( Users userId){
        log.info("Creating new refresh token user: {}",
                userId.getUsername());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userId);
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken.setExpiry_date(Instant.now().plusMillis(refreshTokenDurationMs));

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

        log.debug("Refresh token saved with ID: {} for user: {}",
                savedToken.getId(), userId.getUsername());

        return savedToken;
    }

    public RefreshToken verifyToken (RefreshToken token){
        if (token.getExpiry_date().isBefore(Instant.now())){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Runtime token has expired. Please sign in again.");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token){
        log.info("Searching for refresh token: {}", token);
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByUserId (Users userId){
        log.info("Deleting refresh tokens for user:{}",
                userId.getUsername());
        refreshTokenRepository.deleteByUserId(userId.getId());
    }
}
