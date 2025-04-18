package com.ecommerce.ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "2qCdZxSHSnUW/xdSvwpmqvkRYUQc3jcqPOlTSEFQje8=";

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUsername (String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }


    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return buildToken(claims, userDetails.getUsername());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired (String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration( String token){
        return extractClaim(token, Claims::getExpiration);
    }


    private String buildToken(Map<String, Object> extraClaims, String username){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
