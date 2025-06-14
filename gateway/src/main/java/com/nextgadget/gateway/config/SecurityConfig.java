package com.nextgadget.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                // Public endpoints
                .pathMatchers("/api/users/register", "/api/users/login").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                // Admin-only product management
                .pathMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                // All other endpoints require authentication
                .anyExchange().authenticated()
                .and()
                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    public AuthenticationWebFilter jwtAuthenticationFilter() {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        AuthenticationWebFilter filter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        filter.setServerAuthenticationConverter(exchange -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("Missing or invalid Authorization header");
                return Mono.empty();
            }

            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                Object rolesClaim = claims.get("roles");
                if (rolesClaim == null) {
                    rolesClaim = claims.get("role"); // Fallback if "roles" is missing
                }

                List<String> roles;

                if (rolesClaim instanceof String) {
                    roles = List.of(((String) rolesClaim).split(","));
                } else if (rolesClaim instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> roleList = (List<String>) rolesClaim;
                    roles = roleList.stream().map(String::valueOf).collect(Collectors.toList());
                } else {
                    roles = List.of();
                }

                System.out.println("Token: " + token);
                System.out.println("Username: " + username);
                System.out.println("Roles list: " + roles);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role.trim()))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, token, authorities);
                return Mono.just(auth);

            } catch (JwtException e) {
                System.out.println("JWT Exception: " + e.getMessage());
                return Mono.empty();
            }
        });

        return filter;
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> {

            return Mono.just(authentication);
        };
    }
}
