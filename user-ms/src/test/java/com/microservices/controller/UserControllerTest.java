package com.microservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.dto.LoginDTO;
import com.microservices.dto.LoginResponseDTO;
import com.microservices.dto.UserRequestDTO;
import com.microservices.dto.UserResponseDTO;
import com.microservices.entity.UserRole;
import com.microservices.enums.ErrorCode;
import com.microservices.exception.InvalidCredentialsException;
import com.microservices.service.AuthorizationService;
import com.microservices.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for UserController
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorizationService authorizationService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private LoginDTO loginDTO;
    private LoginResponseDTO loginResponseDTO;

    @BeforeEach
    void setUp() {
        userRequestDTO = UserRequestDTO.builder()
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        userResponseDTO = UserResponseDTO.builder()
                .userId(1L)
                .userName("testuser")
                .email("test@example.com")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();

        loginDTO = LoginDTO.builder()
                .userName("testuser")
                .password("password123")
                .build();

        loginResponseDTO = LoginResponseDTO.builder()
                .token("test-jwt-token")
                .tokenType("Bearer")
                .expiresIn(3600000L)
                .userId(1L)
                .userName("testuser")
                .email("test@example.com")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() throws Exception {
        // Given
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @DisplayName("Should return bad request for invalid user data")
    void shouldReturnBadRequestForInvalidUserData() throws Exception {
        // Given
        UserRequestDTO invalidUser = UserRequestDTO.builder()
                .userName("") // Invalid - empty username
                .email("invalid-email") // Invalid email format
                .password("123") // Invalid - too short password
                .name("") // Invalid - empty name
                .lastName("") // Invalid - empty last name
                .role(null) // Invalid - null role
                .build();

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully() throws Exception {
        // Given
        when(userService.login(any(LoginDTO.class))).thenReturn(loginResponseDTO);

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600000L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Should return unauthorized for invalid login")
    void shouldReturnUnauthorizedForInvalidLogin() throws Exception {
        // Given
        when(userService.login(any(LoginDTO.class)))
                .thenThrow(new InvalidCredentialsException("Invalid credentials", ErrorCode.INVALID_CREDENTIALS));

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return bad request for invalid login data")
    void shouldReturnBadRequestForInvalidLoginData() throws Exception {
        // Given
        LoginDTO invalidLogin = LoginDTO.builder()
                .userName("") // Invalid - empty username
                .password("123") // Invalid - too short password
                .build();

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLogin)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return forbidden for protected endpoints without authentication")
    void shouldReturnForbiddenForProtectedEndpointsWithoutAuthentication() throws Exception {
        // When & Then - Protected endpoints should return 403 without proper authentication
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should handle malformed JSON requests")
    void shouldHandleMalformedJsonRequests() throws Exception {
        // When & Then - Malformed JSON
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json }"))
                .andExpect(status().isInternalServerError());

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json }"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should handle missing content type")
    void shouldHandleMissingContentType() throws Exception {
        // Given
        UserRequestDTO userRequest = UserRequestDTO.builder()
                .userName("testuser")
                .email("test@example.com")
                .password("password123")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .build();

        // When & Then - Missing content type
        mockMvc.perform(post("/api/users")
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should handle empty request body")
    void shouldHandleEmptyRequestBody() throws Exception {
        // When & Then - Empty request body
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isInternalServerError());

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isInternalServerError());
    }
}