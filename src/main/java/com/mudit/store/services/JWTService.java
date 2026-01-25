package com.mudit.store.services;

import com.mudit.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    @Value("${spring.jwt.secret}")
    private String secret;

    public String generateAccessToken(User user) {
        final long tokenExpiration = 300;
        return generateAccessToken(user, tokenExpiration);
    }

    public String generateRefreshToken(User user) {
        final long tokenExpiration = 604800;
        return generateAccessToken(user, tokenExpiration);
    }

    private String generateAccessToken(User user, long tokenExpiration) {
        return Jwts
                .builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .subject(user.getId().toString())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration().after(new Date());
        } catch (JwtException exception) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }
}
