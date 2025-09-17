package com.microservices.service;

import com.microservices.dto.LoginDTO;
import com.microservices.dto.UserRequestDTO;
import com.microservices.dto.UserResponseDTO;
import com.microservices.entity.User;
import com.microservices.entity.UserRole;
import com.microservices.mapper.UserMapper;
import com.microservices.repository.UserRepository;
import com.microservices.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for UserService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
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
    void shouldCreateUserSuccessfully() {
        // Given
        when(userMapper.toEntity(userRequestDTO)).thenReturn(testUser);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toResponseDTO(testUser)).thenReturn(userResponseDTO);

        // When
        UserResponseDTO result = userService.createUser(userRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("testuser", result.getUserName());
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should get user by id")
    void shouldGetUserById() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toResponseDTO(testUser)).thenReturn(userResponseDTO);

        // When
        UserResponseDTO result = userService.getUser(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("testuser", result.getUserName());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should get all users")
    void shouldGetAllUsers() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponseDTO(testUser)).thenReturn(userResponseDTO);

        // When
        List<UserResponseDTO> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully() {
        // Given
        when(userRepository.findByUserNameAndPassword("testuser", "password123"))
                .thenReturn(Optional.of(testUser));

        // When
        boolean result = userService.login(loginDTO);

        // Then
        assertTrue(result);
        verify(userRepository).findByUserNameAndPassword("testuser", "password123");
    }

    @Test
    @DisplayName("Should return false when login fails")
    void shouldReturnFalseWhenLoginFails() {
        // Given
        when(userRepository.findByUserNameAndPassword("testuser", "password123"))
                .thenReturn(Optional.empty());

        // When
        boolean result = userService.login(loginDTO);

        // Then
        assertFalse(result);
        verify(userRepository).findByUserNameAndPassword("testuser", "password123");
    }
}
