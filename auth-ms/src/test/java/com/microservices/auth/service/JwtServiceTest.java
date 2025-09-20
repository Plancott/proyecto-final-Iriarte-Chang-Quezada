package com.microservices.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long EXPIRATION = 86400000L; // 24 horas

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);
        
        userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
    }

    @Test
    void testExtractUserName_ValidToken_ShouldReturnUsername() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        String extractedUsername = jwtService.extractUserName(token);

        // Then
        assertEquals("testuser", extractedUsername);
    }

    @Test
    void testExtractUserName_InvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractUserName(invalidToken));
    }

    @Test
    void testExtractClaim_ValidToken_ShouldReturnClaim() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        String subject = jwtService.extractClaim(token, Claims::getSubject);

        // Then
        assertEquals("testuser", subject);
    }

    @Test
    void testExtractClaim_InvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractClaim(invalidToken, Claims::getSubject));
    }

    @Test
    void testGenerateToken_WithUserDetails_ShouldGenerateValidToken() {
        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("testuser", jwtService.extractUserName(token));
    }

    @Test
    void testGenerateToken_WithExtraClaims_ShouldGenerateTokenWithClaims() {
        // Given
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("userId", 123L);

        // When
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("testuser", jwtService.extractUserName(token));
        assertEquals("ADMIN", jwtService.extractClaim(token, claims -> claims.get("role", String.class)));
        assertEquals(Long.valueOf(123L), jwtService.extractClaim(token, claims -> claims.get("userId", Long.class)));
    }

    @Test
    void testIsTokenValid_ValidTokenAndUser_ShouldReturnTrue() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testIsTokenValid_ValidTokenButDifferentUser_ShouldReturnFalse() {
        // Given
        String token = jwtService.generateToken(userDetails);
        UserDetails differentUser = User.builder()
                .username("differentuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        // When
        boolean isValid = jwtService.isTokenValid(token, differentUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testIsTokenValid_ExpiredToken_ShouldReturnFalse() {
        // Given
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L); // Token expirado
        String expiredToken = jwtService.generateToken(userDetails);

        // When & Then
        assertThrows(Exception.class, () -> jwtService.isTokenValid(expiredToken, userDetails));
    }

    @Test
    void testIsTokenExpired_ValidToken_ShouldReturnFalse() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        boolean isExpired = jwtService.isTokenExpired(token);

        // Then
        assertFalse(isExpired);
    }

    @Test
    void testIsTokenExpired_ExpiredToken_ShouldReturnTrue() {
        // Given
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L); // Token expirado
        String expiredToken = jwtService.generateToken(userDetails);

        // When & Then
        assertThrows(Exception.class, () -> jwtService.isTokenExpired(expiredToken));
    }

    @Test
    void testIsTokenExpired_InvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThrows(Exception.class, () -> jwtService.isTokenExpired(invalidToken));
    }

    @Test
    void testExtractExpiration_ValidToken_ShouldReturnFutureDate() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testExtractAllClaims_ValidToken_ShouldReturnClaims() {
        // Given
        String token = jwtService.generateToken(userDetails);

        // When
        Claims extractedClaims = jwtService.extractClaim(token, claims -> claims);

        // Then
        assertNotNull(extractedClaims);
        assertEquals("testuser", extractedClaims.getSubject());
        assertNotNull(extractedClaims.getIssuedAt());
        assertNotNull(extractedClaims.getExpiration());
    }

    @Test
    void testGetSignInKey_ShouldReturnValidSecretKey() {
        // When
        SecretKey signInKey = (SecretKey) ReflectionTestUtils.invokeMethod(jwtService, "getSignInKey");

        // Then
        assertNotNull(signInKey);
        assertEquals("HmacSHA384", signInKey.getAlgorithm());
    }

    @Test
    void testTokenGeneration_WithDifferentExpirationTimes_ShouldGenerateCorrectTokens() {
        // Given
        long shortExpiration = 1000L; // 1 segundo
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", shortExpiration);

        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertFalse(jwtService.isTokenExpired(token));

        // Wait for token to expire
        try {
            Thread.sleep(1100);
            assertThrows(Exception.class, () -> jwtService.isTokenExpired(token));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void testTokenWithSpecialCharacters_ShouldHandleCorrectly() {
        // Given
        UserDetails specialUser = User.builder()
                .username("user@domain.com")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        // When
        String token = jwtService.generateToken(specialUser);

        // Then
        assertNotNull(token);
        assertEquals("user@domain.com", jwtService.extractUserName(token));
    }

    @Test
    void testTokenWithEmptyClaims_ShouldGenerateValidToken() {
        // Given
        Map<String, Object> emptyClaims = new HashMap<>();

        // When
        String token = jwtService.generateToken(emptyClaims, userDetails);

        // Then
        assertNotNull(token);
        assertEquals("testuser", jwtService.extractUserName(token));
    }

    @Test
    void testTokenWithNullClaims_ShouldGenerateValidToken() {
        // When
        String token = jwtService.generateToken(null, userDetails);

        // Then
        assertNotNull(token);
        assertEquals("testuser", jwtService.extractUserName(token));
    }
}
