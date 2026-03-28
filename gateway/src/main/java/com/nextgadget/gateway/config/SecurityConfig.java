package com.nextgadget.gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        // Public endpoints
                        .pathMatchers("/api/users/register", "/api/users/login").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/notifications/**").permitAll()
                        // Admin-only product management
                        .pathMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
                        // All other endpoints require authentication
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private AuthenticationWebFilter jwtAuthenticationFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        filter.setServerAuthenticationConverter(jwtAuthenticationConverter());
        return filter;
    }

    private ServerAuthenticationConverter jwtAuthenticationConverter() {
        return exchange -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Mono.empty();
            }

            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String username = claims.getSubject();
                String userId = username; // subject holds the username/userId

                Object rolesClaim = claims.get("roles");
                if (rolesClaim == null) {
                    rolesClaim = claims.get("role");
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

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role.trim()))
                        .collect(Collectors.toList());

                // Store userId and roles as exchange attributes for header mutation
                exchange.getAttributes().put("X-User-Id", userId);
                exchange.getAttributes().put("X-User-Roles", String.join(",", roles));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, token, authorities);
                return Mono.just(auth);

            } catch (JwtException e) {
                return Mono.empty();
            }
        };
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> Mono.just(authentication);
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * GlobalFilter that mutates the request to inject X-User-Id and X-User-Roles
     * headers for downstream services after JWT authentication.
     */
    @Bean
    public org.springframework.cloud.gateway.filter.GlobalFilter userHeaderGlobalFilter() {
        return (exchange, chain) -> {
            String userId = exchange.getAttribute("X-User-Id");
            String userRoles = exchange.getAttribute("X-User-Roles");

            if (userId != null) {
                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(r -> r.headers(headers -> {
                            headers.set("X-User-Id", userId);
                            if (userRoles != null) {
                                headers.set("X-User-Roles", userRoles);
                            }
                        }))
                        .build();
                return chain.filter(mutatedExchange);
            }

            return chain.filter(exchange);
        };
    }
}
