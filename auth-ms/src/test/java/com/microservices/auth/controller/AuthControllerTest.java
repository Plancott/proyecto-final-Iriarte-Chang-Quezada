package com.microservices.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.auth.dto.TokenValidationResponse;
import com.microservices.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testValidateToken_ValidBearerToken_ShouldReturnValidResponse() throws Exception {
        // Given
        String bearerToken = "Bearer valid.jwt.token";
        String jwtToken = "valid.jwt.token";
        TokenValidationResponse expectedResponse = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role("USER")
                .userId(123L)
                .build();

        when(authService.validateToken(jwtToken)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.userId").value(123));

        verify(authService).validateToken(jwtToken);
    }

    @Test
    void testValidateToken_InvalidBearerToken_ShouldReturnInvalidResponse() throws Exception {
        // Given
        String bearerToken = "Bearer invalid.jwt.token";
        String jwtToken = "invalid.jwt.token";
        TokenValidationResponse expectedResponse = TokenValidationResponse.builder()
                .valid(false)
                .build();

        when(authService.validateToken(jwtToken)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.userName").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist())
                .andExpect(jsonPath("$.userId").doesNotExist());

        verify(authService).validateToken(jwtToken);
    }

    @Test
    void testValidateToken_ExpiredToken_ShouldReturnInvalidResponse() throws Exception {
        // Given
        String bearerToken = "Bearer expired.jwt.token";
        String jwtToken = "expired.jwt.token";
        TokenValidationResponse expectedResponse = TokenValidationResponse.builder()
                .valid(false)
                .build();

        when(authService.validateToken(jwtToken)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(false));

        verify(authService).validateToken(jwtToken);
    }

    @Test
    void testValidateToken_ValidTokenWithNullValues_ShouldReturnValidResponse() throws Exception {
        // Given
        String bearerToken = "Bearer valid.jwt.token";
        String jwtToken = "valid.jwt.token";
        TokenValidationResponse expectedResponse = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role(null)
                .userId(null)
                .build();

        when(authService.validateToken(jwtToken)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.role").doesNotExist())
                .andExpect(jsonPath("$.userId").doesNotExist());

        verify(authService).validateToken(jwtToken);
    }

    @Test
    void testValidateToken_EmptyBearerToken_ShouldReturnInvalidResponse() throws Exception {
        // Given
        String bearerToken = "Bearer ";
        String jwtToken = "";
        TokenValidationResponse expectedResponse = TokenValidationResponse.builder()
                .valid(false)
                .build();

        when(authService.validateToken(jwtToken)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(false));

        verify(authService).validateToken(jwtToken);
    }

    @Test
    void testValidateToken_NoBearerPrefix_ShouldReturnInvalidResponse() throws Exception {
        // Given
        String token = "valid.jwt.token";
        String jwtToken = "valid.jwt.token";
        TokenValidationResponse expectedResponse = TokenValidationResponse.builder()
                .valid(false)
                .build();

        when(authService.validateToken(anyString())).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(false));

        verify(authService).validateToken(anyString());
    }

    @Test
    void testValidateToken_MissingAuthorizationHeader_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(authService, never()).validateToken(anyString());
    }

    @Test
    void testValidateToken_NullAuthorizationHeader_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(authService, never()).validateToken(anyString());
    }

    @Test
    void testValidateToken_ServiceThrowsException_ShouldReturnServerError() throws Exception {
        // Given
        String bearerToken = "Bearer valid.jwt.token";
        String jwtToken = "valid.jwt.token";

        when(authService.validateToken(anyString())).thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(authService).validateToken(anyString());
    }

    @Test
    void testValidateToken_ValidTokenWithSpecialCharacters_ShouldReturnValidResponse() throws Exception {
        // Given
        String bearerToken = "Bearer special.token.with@chars";
        String jwtToken = "special.token.with@chars";
        TokenValidationResponse expectedResponse = TokenValidationResponse.builder()
                .valid(true)
                .userName("user@domain.com")
                .role("ADMIN")
                .userId(456L)
                .build();

        when(authService.validateToken(jwtToken)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.userName").value("user@domain.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.userId").value(456));

        verify(authService).validateToken(jwtToken);
    }

    @Test
    void testValidateToken_ValidTokenWithLongValues_ShouldReturnValidResponse() throws Exception {
        // Given
        String bearerToken = "Bearer very.long.token.with.many.segments";
        String jwtToken = "very.long.token.with.many.segments";
        TokenValidationResponse expectedResponse = TokenValidationResponse.builder()
                .valid(true)
                .userName("verylongusernamethatexceedsnormallength")
                .role("VERY_LONG_ROLE_NAME_THAT_EXCEEDS_NORMAL_LENGTH")
                .userId(Long.MAX_VALUE)
                .build();

        when(authService.validateToken(jwtToken)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.userName").value("verylongusernamethatexceedsnormallength"))
                .andExpect(jsonPath("$.role").value("VERY_LONG_ROLE_NAME_THAT_EXCEEDS_NORMAL_LENGTH"))
                .andExpect(jsonPath("$.userId").value(Long.MAX_VALUE));

        verify(authService).validateToken(jwtToken);
    }

    @Test
    void testValidateToken_ValidTokenWithEmptyStringValues_ShouldReturnValidResponse() throws Exception {
        // Given
        String bearerToken = "Bearer valid.jwt.token";
        String jwtToken = "valid.jwt.token";
        TokenValidationResponse expectedResponse = TokenValidationResponse.builder()
                .valid(true)
                .userName("")
                .role("")
                .userId(0L)
                .build();

        when(authService.validateToken(jwtToken)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.userName").value(""))
                .andExpect(jsonPath("$.role").value(""))
                .andExpect(jsonPath("$.userId").value(0));

        verify(authService).validateToken(jwtToken);
    }

    @Test
    void testValidateToken_ValidTokenWithNegativeUserId_ShouldReturnValidResponse() throws Exception {
        // Given
        String bearerToken = "Bearer valid.jwt.token";
        String jwtToken = "valid.jwt.token";
        TokenValidationResponse expectedResponse = TokenValidationResponse.builder()
                .valid(true)
                .userName("testuser")
                .role("USER")
                .userId(-1L)
                .build();

        when(authService.validateToken(jwtToken)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.userId").value(-1));

        verify(authService).validateToken(jwtToken);
    }
}
