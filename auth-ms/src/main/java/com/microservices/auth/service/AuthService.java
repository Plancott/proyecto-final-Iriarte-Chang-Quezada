package com.microservices.auth.service;

import com.microservices.auth.dto.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtService jwtService;

    public TokenValidationResponse validateToken(String token) {
        try {
            // Extraer información del token
            String userName = jwtService.extractUserName(token);
            boolean isValid = !jwtService.isTokenExpired(token);
            
            if (isValid) {
                // Extraer rol del token
                String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
                Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));
                
                return TokenValidationResponse.builder()
                    .valid(true)
                    .userName(userName)
                    .role(role)
                    .userId(userId)
                    .build();
            } else {
                return TokenValidationResponse.builder()
                    .valid(false)
                    .build();
            }
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return TokenValidationResponse.builder()
                .valid(false)
                .build();
        }
    }
}
