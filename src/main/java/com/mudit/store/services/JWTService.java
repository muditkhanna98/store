package com.mudit.store.services;

import com.mudit.store.config.JwtConfig;
import com.mudit.store.entities.Role;
import com.mudit.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JWTService {
    private final JwtConfig jwtConfig;


    public String generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private String generateToken(User user, long tokenExpiration) {
        return Jwts.builder().issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration)).subject(user.getId().toString()).signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes())).claim("name", user.getName()).claim("email", user.getEmail()).claim("role", user.getRole()).compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes())).build().parseSignedClaims(token).getPayload();

            return claims.getExpiration().after(new Date());
        } catch (JwtException exception) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes())).build().parseSignedClaims(token).getPayload();

        return Long.valueOf(claims.getSubject());
    }

    public Role getRoleFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes())).build().parseSignedClaims(token).getPayload();
        return Role.valueOf(String.valueOf(claims.get("role")));
    }
}
