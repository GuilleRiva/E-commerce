package com.ecommerce.ecommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        log.debug("Generating signing key ...");
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUsername (String token){
        String username = extractClaim(token,
                Claims::getSubject);
        log.info("Extracted username from token: {}",
                username);
        return username;
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }


    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()
                + 1000 * 60 * 60 * 24 * 7 ))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        boolean isValid = username.equals(userDetails.getUsername())&& !
                isTokenExpired(token);

        if (isValid){
            log.info("Token is valid for user: {}", username);
        }else {
            log.warn("Invalid token for user: {}", username);
        }
        return isValid;
    }

    private boolean isTokenExpired (String token){
        boolean expired = extractExpiration(token).before(new Date());
        if (expired){
            log.warn("Token has expired");
        }
        return expired;
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
       try{
           log.debug("Extracting all claims from token...");
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
       } catch (Exception e){
           log.error("Failed to extract claims from token", e);
           throw new RuntimeException("Invalid token");
       }
    }
}
