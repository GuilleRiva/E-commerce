package com.ecommerce.ecommerce.auth;

import com.ecommerce.ecommerce.dto.XRequestDTO.RefreshTokenRequest;
import com.ecommerce.ecommerce.dto.XResponseDTO.RefreshTokenResponse;
import com.ecommerce.ecommerce.model.RefreshToken;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.RefreshTokenRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.security.JwtService;
import com.ecommerce.ecommerce.security.RefreshTokenService;
import com.ecommerce.ecommerce.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;


    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public AuthResponse login (AuthRequest request) {
        log.info("Starting authentication process for user: {}",
                request.getUsername());
        try{
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(),
                                request.getPassword())
                );
        log.info("Authentication successful for user: {}",
                request.getUsername());

        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.error("User not found after successful authentication.{}",
                            request.getUsername());
                    return new RuntimeException("User not found");
                });

        String accessToken = jwtService.generateToken((UserDetails) user);
        String refreshToken = jwtService.generateToken((UserDetails) user);

        log.info("JWT and Refresh Token generated for user: {}",
                user.getUsername());

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(refreshToken);
        token.setExpiry_date(Instant.now().plus(7, ChronoUnit.DAYS));

        refreshTokenRepository.save(token);

        return new AuthResponse(accessToken, refreshToken);
    }catch (AuthenticationException e){
            log.warn("Invalid login attempt for user:{}",
                    request.getUsername());
            throw new RuntimeException("Invalid username or password");
        }
    }


    public RefreshTokenResponse refreshTokenResponse(RefreshTokenRequest request){
        String requestTokenValue = request.getRefreshTokenRequest();

        log.info("Starting refresh token process for token: {}",
                requestTokenValue);

        RefreshToken refreshToken = refreshTokenService.findByToken(requestTokenValue)
                .orElseThrow(()-> {
                    log.warn("Refresh token not found: {}",
                            requestTokenValue);
                    return new RuntimeException("Refresh token not found");
                        });

        refreshTokenService.verifyToken(refreshToken);
        log.debug("Refresh token verified: {}", requestTokenValue);

        Users user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(new UserDetailsImpl(user));
        log.info("Generated new access token for user: {}",
                user.getUsername());

        return new RefreshTokenResponse(accessToken, requestTokenValue, "Bearer");
    }
}
