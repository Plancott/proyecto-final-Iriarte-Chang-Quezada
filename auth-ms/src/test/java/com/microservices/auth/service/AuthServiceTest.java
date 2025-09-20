package com.microservices.auth.service;

import com.microservices.auth.dto.TokenValidationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private final String VALID_TOKEN = "valid.jwt.token";
    private final String INVALID_TOKEN = "invalid.token";
    private final String USERNAME = "testuser";
    private final String ROLE = "USER";
    private final Long USER_ID = 123L;

    @BeforeEach
    void setUp() {
        // Setup común para todos los tests
    }

    @Test
    void testValidateToken_ValidToken_ShouldReturnValidResponse() {
        // Given
        when(jwtService.extractUserName(VALID_TOKEN)).thenReturn(USERNAME);
        when(jwtService.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractClaim(eq(VALID_TOKEN), any())).thenReturn(ROLE).thenReturn(USER_ID);

        // When
        TokenValidationResponse response = authService.validateToken(VALID_TOKEN);

        // Then
        assertNotNull(response);
        assertTrue(response.isValid());
        assertEquals(USERNAME, response.getUserName());
        assertEquals(ROLE, response.getRole());
        assertEquals(USER_ID, response.getUserId());

        verify(jwtService).extractUserName(VALID_TOKEN);
        verify(jwtService).isTokenExpired(VALID_TOKEN);
        verify(jwtService, times(2)).extractClaim(eq(VALID_TOKEN), any());
    }

    @Test
    void testValidateToken_ExpiredToken_ShouldReturnInvalidResponse() {
        // Given
        when(jwtService.extractUserName(VALID_TOKEN)).thenReturn(USERNAME);
        when(jwtService.isTokenExpired(VALID_TOKEN)).thenReturn(true);

        // When
        TokenValidationResponse response = authService.validateToken(VALID_TOKEN);

        // Then
        assertNotNull(response);
        assertFalse(response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());

        verify(jwtService).extractUserName(VALID_TOKEN);
        verify(jwtService).isTokenExpired(VALID_TOKEN);
        verify(jwtService, never()).extractClaim(anyString(), any());
    }

    @Test
    void testValidateToken_InvalidToken_ShouldReturnInvalidResponse() {
        // Given
        when(jwtService.extractUserName(INVALID_TOKEN)).thenThrow(new RuntimeException("Invalid token"));

        // When
        TokenValidationResponse response = authService.validateToken(INVALID_TOKEN);

        // Then
        assertNotNull(response);
        assertFalse(response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());

        verify(jwtService).extractUserName(INVALID_TOKEN);
        verify(jwtService, never()).isTokenExpired(anyString());
        verify(jwtService, never()).extractClaim(anyString(), any());
    }

    @Test
    void testValidateToken_ValidTokenWithNullRole_ShouldReturnValidResponseWithNullRole() {
        // Given
        when(jwtService.extractUserName(VALID_TOKEN)).thenReturn(USERNAME);
        when(jwtService.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractClaim(eq(VALID_TOKEN), any())).thenReturn(null).thenReturn(USER_ID);

        // When
        TokenValidationResponse response = authService.validateToken(VALID_TOKEN);

        // Then
        assertNotNull(response);
        assertTrue(response.isValid());
        assertEquals(USERNAME, response.getUserName());
        assertNull(response.getRole());
        assertEquals(USER_ID, response.getUserId());
    }

    @Test
    void testValidateToken_ValidTokenWithNullUserId_ShouldReturnValidResponseWithNullUserId() {
        // Given
        when(jwtService.extractUserName(VALID_TOKEN)).thenReturn(USERNAME);
        when(jwtService.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractClaim(eq(VALID_TOKEN), any())).thenReturn(ROLE).thenReturn(null);

        // When
        TokenValidationResponse response = authService.validateToken(VALID_TOKEN);

        // Then
        assertNotNull(response);
        assertTrue(response.isValid());
        assertEquals(USERNAME, response.getUserName());
        assertEquals(ROLE, response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testValidateToken_ValidTokenWithEmptyUsername_ShouldReturnValidResponse() {
        // Given
        when(jwtService.extractUserName(VALID_TOKEN)).thenReturn("");
        when(jwtService.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractClaim(eq(VALID_TOKEN), any())).thenReturn(ROLE).thenReturn(USER_ID);

        // When
        TokenValidationResponse response = authService.validateToken(VALID_TOKEN);

        // Then
        assertNotNull(response);
        assertTrue(response.isValid());
        assertEquals("", response.getUserName());
        assertEquals(ROLE, response.getRole());
        assertEquals(USER_ID, response.getUserId());
    }

    @Test
    void testValidateToken_ExceptionDuringTokenValidation_ShouldReturnInvalidResponse() {
        // Given
        when(jwtService.extractUserName(VALID_TOKEN)).thenThrow(new RuntimeException("Token validation error"));

        // When
        TokenValidationResponse response = authService.validateToken(VALID_TOKEN);

        // Then
        assertNotNull(response);
        assertFalse(response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testValidateToken_ExceptionDuringExpirationCheck_ShouldReturnInvalidResponse() {
        // Given
        when(jwtService.extractUserName(VALID_TOKEN)).thenReturn(USERNAME);
        when(jwtService.isTokenExpired(VALID_TOKEN)).thenThrow(new RuntimeException("Expiration check error"));

        // When
        TokenValidationResponse response = authService.validateToken(VALID_TOKEN);

        // Then
        assertNotNull(response);
        assertFalse(response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testValidateToken_ExceptionDuringClaimExtraction_ShouldReturnInvalidResponse() {
        // Given
        when(jwtService.extractUserName(VALID_TOKEN)).thenReturn(USERNAME);
        when(jwtService.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractClaim(eq(VALID_TOKEN), any())).thenThrow(new RuntimeException("Claim extraction error"));

        // When
        TokenValidationResponse response = authService.validateToken(VALID_TOKEN);

        // Then
        assertNotNull(response);
        assertFalse(response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testValidateToken_NullToken_ShouldReturnInvalidResponse() {
        // Given
        when(jwtService.extractUserName(null)).thenThrow(new RuntimeException("Null token"));

        // When
        TokenValidationResponse response = authService.validateToken(null);

        // Then
        assertNotNull(response);
        assertFalse(response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testValidateToken_EmptyToken_ShouldReturnInvalidResponse() {
        // Given
        when(jwtService.extractUserName("")).thenThrow(new RuntimeException("Empty token"));

        // When
        TokenValidationResponse response = authService.validateToken("");

        // Then
        assertNotNull(response);
        assertFalse(response.isValid());
        assertNull(response.getUserName());
        assertNull(response.getRole());
        assertNull(response.getUserId());
    }

    @Test
    void testValidateToken_ValidTokenWithSpecialCharacters_ShouldReturnValidResponse() {
        // Given
        String specialUsername = "user@domain.com";
        when(jwtService.extractUserName(VALID_TOKEN)).thenReturn(specialUsername);
        when(jwtService.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractClaim(eq(VALID_TOKEN), any())).thenReturn(ROLE).thenReturn(USER_ID);

        // When
        TokenValidationResponse response = authService.validateToken(VALID_TOKEN);

        // Then
        assertNotNull(response);
        assertTrue(response.isValid());
        assertEquals(specialUsername, response.getUserName());
        assertEquals(ROLE, response.getRole());
        assertEquals(USER_ID, response.getUserId());
    }

    @Test
    void testValidateToken_ValidTokenWithLongValues_ShouldReturnValidResponse() {
        // Given
        String longUsername = "verylongusernamethatexceedsnormallength";
        String longRole = "VERY_LONG_ROLE_NAME_THAT_EXCEEDS_NORMAL_LENGTH";
        Long longUserId = Long.MAX_VALUE;

        when(jwtService.extractUserName(VALID_TOKEN)).thenReturn(longUsername);
        when(jwtService.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(jwtService.extractClaim(eq(VALID_TOKEN), any())).thenReturn(longRole).thenReturn(longUserId);

        // When
        TokenValidationResponse response = authService.validateToken(VALID_TOKEN);

        // Then
        assertNotNull(response);
        assertTrue(response.isValid());
        assertEquals(longUsername, response.getUserName());
        assertEquals(longRole, response.getRole());
        assertEquals(longUserId, response.getUserId());
    }
}