package com.nextgadget.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public String extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object rolesClaim = claims.get("roles");
        if (rolesClaim == null) {
            rolesClaim = claims.get("role");
        }

        if (rolesClaim == null) {
            return Collections.emptyList();
        }

        if (rolesClaim instanceof String) {
            // If roles stored as comma-separated string, split it
            String rolesStr = (String) rolesClaim;
            return Arrays.stream(rolesStr.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        if (rolesClaim instanceof List<?>) {
            // If roles stored as list in claims
            @SuppressWarnings("unchecked")
            List<String> rolesList = (List<String>) rolesClaim;
            return rolesList;
        }

        // Fallback empty list
        return Collections.emptyList();
    }
}
