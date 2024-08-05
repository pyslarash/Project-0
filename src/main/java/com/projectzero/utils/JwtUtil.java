package com.projectzero.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.util.Date;
import java.util.Base64;
import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private String expTime;

    public String generateToken(String login) {
        try {

            long expirationMillis = Long.parseLong(expTime);
            Date expirationDate = new Date(System.currentTimeMillis() + expirationMillis);

            JwtBuilder builder = Jwts.builder()
                    .setSubject(login)
                    .setIssuedAt(new Date())
                    .setExpiration(expirationDate)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256);

            return builder.compact();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid JWT expiration time format", e);
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractClaims(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build();
            return parser.parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public String extractLogin(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String login) {
        return (login.equals(extractLogin(token)) && !isTokenExpired(token));
    }
}
