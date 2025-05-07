package com.ecommerce.ecommerce.auth;

import com.ecommerce.ecommerce.dto.XRequestDTO.RefreshTokenRequest;
import com.ecommerce.ecommerce.dto.XResponseDTO.RefreshTokenResponse;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.security.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, JwtService jwtService, UserRepository userRepository) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse>
    login(@RequestBody AuthRequest request){
        log.info("Attempting login for user :{}",
                request.getUsername());
        AuthResponse response = authService.login(request);
        log.info("Login successful for user: {}",
                request.getUsername());
        return ResponseEntity.ok(authService.login(request));
    }



    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse>
    refreshToken(@RequestBody RefreshTokenRequest request){
        log.info("Attempting for refresh token");

        String username = jwtService.extractUsername(request.getRefreshTokenRequest());

        Users user = userRepository.findByUsername(username)
                .orElseThrow(()-> {
                    log.error("User not found with username: {}",
                            username);
                    return new RuntimeException("User not found");
                });



        if (!jwtService.isTokenValid(request.getRefreshTokenRequest(), (UserDetails) user)){
            log.warn("Invalid refreshed successfully for user: {}",
                    username);
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken((UserDetails) user);
        log.info("Token refreshed successfully for user: {}",
                username);
        return ResponseEntity.ok(
                new RefreshTokenResponse(newAccessToken,
                        request.getRefreshTokenRequest(), "Bearer")
        );
    }

}
