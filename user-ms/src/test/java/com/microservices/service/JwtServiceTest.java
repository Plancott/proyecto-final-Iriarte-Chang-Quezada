package com.microservices.service;

import com.microservices.entity.User;
import com.microservices.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para JwtService - Generación y validación de tokens JWT
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        
        // Configurar valores de test usando ReflectionTestUtils
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L); // 1 hora para tests
        
        testUser = User.builder()
                .userId(1L)
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should generate token for user successfully")
    void shouldGenerateTokenForUserSuccessfully() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verificar que el token contiene el userName
        String extractedUserName = jwtService.extractUserName(token);
        assertEquals("testuser", extractedUserName);
    }


    @Test
    @DisplayName("Should extract username from token")
    void shouldExtractUsernameFromToken() {
        // Given
        String token = jwtService.generateToken(testUser);

        // When
        String extractedUserName = jwtService.extractUserName(token);

        // Then
        assertEquals("testuser", extractedUserName);
    }

    @Test
    @DisplayName("Should extract role claim from token")
    void shouldExtractRoleClaimFromToken() {
        // Given
        String token = jwtService.generateToken(testUser);

        // When
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));

        // Then
        assertEquals("USER", role);
    }

    @Test
    @DisplayName("Should extract userId claim from token")
    void shouldExtractUserIdClaimFromToken() {
        // Given
        String token = jwtService.generateToken(testUser);

        // When
        Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));

        // Then
        assertEquals(1L, userId);
    }

    @Test
    @DisplayName("Should handle expired token gracefully")
    void shouldHandleExpiredTokenGracefully() {
        // Given - Crear un token con expiración muy corta
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L); // Token expirado
        String expiredToken = jwtService.generateToken(testUser);
        
        // Restaurar expiración normal
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);

        // When & Then - Debe lanzar excepción al verificar si está expirado
        assertThrows(Exception.class, () -> jwtService.isTokenExpired(expiredToken));
    }

    @Test
    @DisplayName("Should return true for valid non-expired token")
    void shouldReturnTrueForValidNonExpiredToken() {
        // Given
        String token = jwtService.generateToken(testUser);

        // When
        boolean isExpired = jwtService.isTokenExpired(token);

        // Then
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Should throw exception for invalid token format")
    void shouldThrowExceptionForInvalidTokenFormat() {
        // Given
        String invalidToken = "invalid.token.format";

        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractUserName(invalidToken));
    }

    @Test
    @DisplayName("Should throw exception for null token")
    void shouldThrowExceptionForNullToken() {
        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractUserName(null));
    }

    @Test
    @DisplayName("Should throw exception for empty token")
    void shouldThrowExceptionForEmptyToken() {
        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractUserName(""));
    }

    @Test
    @DisplayName("Should generate valid tokens for same user")
    void shouldGenerateValidTokensForSameUser() {
        // When
        String token1 = jwtService.generateToken(testUser);
        String token2 = jwtService.generateToken(testUser);

        // Then
        // Ambos tokens deben ser válidos
        assertNotNull(token1);
        assertNotNull(token2);
        assertFalse(token1.isEmpty());
        assertFalse(token2.isEmpty());
        
        // Ambos deben tener el mismo userName
        assertEquals(jwtService.extractUserName(token1), jwtService.extractUserName(token2));
        
        // Y el mismo userId
        Long userId1 = jwtService.extractClaim(token1, claims -> claims.get("userId", Long.class));
        Long userId2 = jwtService.extractClaim(token2, claims -> claims.get("userId", Long.class));
        assertEquals(userId1, userId2);
        
        // Y el mismo rol
        String role1 = jwtService.extractClaim(token1, claims -> claims.get("role", String.class));
        String role2 = jwtService.extractClaim(token2, claims -> claims.get("role", String.class));
        assertEquals(role1, role2);
    }

    @Test
    @DisplayName("Should generate token with correct expiration time")
    void shouldGenerateTokenWithCorrectExpirationTime() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertNotNull(token);
        
        // Verificar que el token no está expirado inmediatamente
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    @DisplayName("Should handle token with special characters in claims")
    void shouldHandleTokenWithSpecialCharactersInClaims() {
        // Given
        User userWithSpecialChars = User.builder()
                .userId(2L)
                .userName("user@domain.com")
                .email("user@domain.com")
                .password("password123")
                .name("José María")
                .lastName("González-López")
                .role(UserRole.ADMIN)
                .registerDate(LocalDateTime.now())
                .build();

        // When
        String token = jwtService.generateToken(userWithSpecialChars);

        // Then
        assertNotNull(token);
        String extractedUserName = jwtService.extractUserName(token);
        assertEquals("user@domain.com", extractedUserName);
        
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        assertEquals("ADMIN", role);
    }

    @Test
    @DisplayName("Should generate token with extra claims")
    void shouldGenerateTokenWithExtraClaims() {
        // Given
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "customValue");
        extraClaims.put("anotherClaim", 123);

        // When
        String token = jwtService.generateToken(extraClaims, testUser);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // Verificar que se pueden extraer las claims
        String userName = jwtService.extractUserName(token);
        assertEquals("testuser", userName);
    }

    @Test
    @DisplayName("Should generate token for UserDetails")
    void shouldGenerateTokenForUserDetails() {
        // Given
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        String userName = jwtService.extractUserName(token);
        assertEquals("testuser", userName);
    }

    @Test
    @DisplayName("Should validate token for correct user")
    void shouldValidateTokenForCorrectUser() {
        // Given
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
        String token = jwtService.generateToken(userDetails);

        // When
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should invalidate token for wrong user")
    void shouldInvalidateTokenForWrongUser() {
        // Given
        UserDetails correctUser = org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
        UserDetails wrongUser = org.springframework.security.core.userdetails.User.builder()
                .username("wronguser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
        String token = jwtService.generateToken(correctUser);

        // When
        boolean isValid = jwtService.isTokenValid(token, wrongUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should extract custom claims from token")
    void shouldExtractCustomClaimsFromToken() {
        // Given
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("userId", 123L);
        String token = jwtService.generateToken(extraClaims, testUser);

        // When
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));

        // Then
        assertEquals("ADMIN", role);
        assertEquals(123L, userId);
    }

    @Test
    @DisplayName("Should handle invalid token format")
    void shouldHandleInvalidTokenFormat() {
        // Given
        String invalidToken = "invalid.token.format";

        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractUserName(invalidToken));
        assertThrows(Exception.class, () -> jwtService.isTokenExpired(invalidToken));
    }

    @Test
    @DisplayName("Should handle null token")
    void shouldHandleNullToken() {
        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractUserName(null));
        assertThrows(Exception.class, () -> jwtService.isTokenExpired(null));
    }

    @Test
    @DisplayName("Should handle empty token")
    void shouldHandleEmptyToken() {
        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractUserName(""));
        assertThrows(Exception.class, () -> jwtService.isTokenExpired(""));
    }
}