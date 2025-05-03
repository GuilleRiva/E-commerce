package com.ecommerce.ecommerce.auth;

import com.ecommerce.ecommerce.dto.XRequestDTO.RefreshTokenRequest;
import com.ecommerce.ecommerce.dto.XResponseDTO.RefreshTokenResponse;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.security.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse>
    refreshToken(@RequestBody RefreshTokenRequest request){
        String username = jwtService.extractUsername(request.getRefreshTokenRequest());

        Users user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(request.getRefreshTokenRequest(), (UserDetails) user)){
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken((UserDetails) user);
        return ResponseEntity.ok(
                new RefreshTokenResponse(newAccessToken, request.getRefreshTokenRequest(), "Bearer")
        );
    }

}
