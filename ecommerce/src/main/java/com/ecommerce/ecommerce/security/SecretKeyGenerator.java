package com.ecommerce.ecommerce.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64key = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Generated JWT Secret Key: " + base64key);
    }
}
