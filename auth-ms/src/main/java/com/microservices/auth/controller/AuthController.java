package com.microservices.auth.controller;

import com.microservices.auth.dto.TokenValidationResponse;
import com.microservices.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // Remove "Bearer "
        TokenValidationResponse response = authService.validateToken(jwt);
        return ResponseEntity.ok(response);
    }
}
