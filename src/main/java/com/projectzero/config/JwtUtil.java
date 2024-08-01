package com.projectzero.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        // Initialize the SecretKey from the provided base64-encoded secret key
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        System.out.println("Secret Key: " + secretKey);
        System.out.println("JWT Expiration: " + jwtExpirationInMs);
    }

    public String generateToken(UserDetails userDetails) {
        // Assuming userDetails has a method to get user ID
        String userId = ((CustomUserDetails) userDetails).getUserId();
        return Jwts.builder()
                .setSubject(userId)  // Store user ID as the subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        // Assuming userDetails has a method to get user ID
        String userDetailsId = ((CustomUserDetails) userDetails).getUserId();
        return (userId.equals(userDetailsId) && !isTokenExpired(token));
    }


    public String extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();  // Returns the user ID
    }

    private boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }
}
