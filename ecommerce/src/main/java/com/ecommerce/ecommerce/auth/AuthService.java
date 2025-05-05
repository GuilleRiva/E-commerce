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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

    public AuthResponse login (AuthRequest request){

            //1. Autentica el usuario con su username y password

            Authentication authentication =
                    authenticationManager.authenticate(
                            new  UsernamePasswordAuthenticationToken(request.getUsername(),
                                    request.getPassword())
                    );

            Users user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(()-> new RuntimeException("User not found"));

            String accessToken = jwtService.generateToken((UserDetails) user);
            String refreshToken = jwtService.generateToken((UserDetails) user);

            RefreshToken token = new RefreshToken();
            token.setUser(user);
            token.setToken(refreshToken);
            token.setExpiry_date(Instant.now().plus(7, ChronoUnit.DAYS));

            refreshTokenRepository.save(token);

            return new AuthResponse(accessToken, refreshToken);
    }

    public RefreshTokenResponse refreshTokenResponse(RefreshTokenRequest request){
        String requestToken = request.getRefreshTokenRequest();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestToken)
                .orElseThrow(()-> new RuntimeException("Refresh token not found"));

        refreshTokenService.verifyToken(refreshToken);

        Users user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(new UserDetailsImpl(user));

        return new RefreshTokenResponse(accessToken, requestToken, "Bearer");
    }
}
