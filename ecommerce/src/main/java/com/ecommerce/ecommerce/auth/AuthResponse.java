package com.ecommerce.ecommerce.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Authentication response" +
        "containing JWT token")
@Getter
public class AuthResponse {
    @Schema(description = "JWT token", example =
    "eyHjbpoOiuhasmkasldjj...")
    private final String token;

    public AuthResponse(String token){
        this.token= token;
    }

}
