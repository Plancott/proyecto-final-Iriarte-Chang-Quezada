package com.microservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.dto.LoginDTO;
import com.microservices.dto.UserRequestDTO;
import com.microservices.dto.UserResponseDTO;
import com.microservices.entity.UserRole;
import com.microservices.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
@WebMvcTest(UserController.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private LoginDTO loginDTO;

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
    @DisplayName("Should get user by id successfully")
    void shouldGetUserByIdSuccessfully() throws Exception {
        // Given
        when(userService.getUser(1L)).thenReturn(userResponseDTO);

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @DisplayName("Should get all users successfully")
    void shouldGetAllUsersSuccessfully() throws Exception {
        // Given
        List<UserResponseDTO> users = Arrays.asList(userResponseDTO);
        when(userService.getAllUsers()).thenReturn(users);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].userName").value("testuser"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully() throws Exception {
        // Given
        when(userService.login(any(LoginDTO.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Should return false for invalid login")
    void shouldReturnFalseForInvalidLogin() throws Exception {
        // Given
        when(userService.login(any(LoginDTO.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
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
    @DisplayName("Should return not found for non-existent user")
    void shouldReturnNotFoundForNonExistentUser() throws Exception {
        // Given
        when(userService.getUser(999L)).thenThrow(new RuntimeException("User not found"));

        // When & Then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isInternalServerError());
    }
}
