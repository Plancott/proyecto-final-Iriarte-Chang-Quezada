package com.microservices.service;

import com.microservices.dto.LoginDTO;
import com.microservices.dto.LoginResponseDTO;
import com.microservices.dto.UserRequestDTO;
import com.microservices.dto.UserResponseDTO;
import com.microservices.dto.UserUpdateDTO;
import com.microservices.entity.User;
import com.microservices.entity.UserRole;
import com.microservices.exception.InvalidCredentialsException;
import com.microservices.exception.UserAlreadyExistsException;
import com.microservices.exception.UserNotFoundException;
import com.microservices.mapper.UserMapper;
import com.microservices.repository.UserRepository;
import com.microservices.service.impl.UserServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

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
        when(userRepository.existsByUserName("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(userRequestDTO)).thenReturn(testUser);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toResponseDTO(testUser)).thenReturn(userResponseDTO);

        // When
        UserResponseDTO result = userService.createUser(userRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("testuser", result.getUserName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test", result.getName());
        assertEquals("User", result.getLastName());
        assertEquals(UserRole.USER, result.getRole());
        verify(userRepository).existsByUserName("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(testUser);
        verify(userMapper).toResponseDTO(testUser);
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
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully() {
        // Given
        when(userRepository.findByUserName("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "password123")).thenReturn(true);
        when(jwtService.generateToken(testUser)).thenReturn("test-jwt-token");

        // When
        LoginResponseDTO result = userService.login(loginDTO);

        // Then
        assertNotNull(result);
        assertEquals("test-jwt-token", result.getToken());
        assertEquals("Bearer", result.getTokenType());
        assertEquals(1L, result.getUserId());
        assertEquals("testuser", result.getUserName());
        verify(userRepository).findByUserName("testuser");
        verify(passwordEncoder).matches("password123", "password123");
        verify(jwtService).generateToken(testUser);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByUserName("testuser")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> userService.login(loginDTO));
        verify(userRepository).findByUserName("testuser");
    }

    @Test
    @DisplayName("Should throw exception when password is incorrect")
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        // Given
        when(userRepository.findByUserName("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "password123")).thenReturn(false);

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> userService.login(loginDTO));
        verify(userRepository).findByUserName("testuser");
        verify(passwordEncoder).matches("password123", "password123");
    }

    @Test
    @DisplayName("Should throw exception when creating user with existing userName")
    void shouldThrowExceptionWhenCreatingUserWithExistingUserName() {
        // Given
        when(userRepository.existsByUserName("testuser")).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequestDTO));
        verify(userRepository).existsByUserName("testuser");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when creating user with existing email")
    void shouldThrowExceptionWhenCreatingUserWithExistingEmail() {
        // Given
        when(userRepository.existsByUserName("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequestDTO));
        verify(userRepository).existsByUserName("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user not found for update")
    void shouldThrowExceptionWhenUserNotFoundForUpdate() {
        // Given
        UserUpdateDTO updateDTO = UserUpdateDTO.builder()
                .userName("newuser")
                .email("new@example.com")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, updateDTO));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user not found for delete")
    void shouldThrowExceptionWhenUserNotFoundForDelete() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Given
        UserUpdateDTO updateDTO = UserUpdateDTO.builder()
                .userName("newuser")
                .email("new@example.com")
                .name("New")
                .lastName("Name")
                .build();
        
        User updatedUser = User.builder()
                .userId(1L)
                .userName("newuser")
                .email("new@example.com")
                .password("encodedPassword")
                .name("New")
                .lastName("Name")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();

        UserResponseDTO updatedResponseDTO = UserResponseDTO.builder()
                .userId(1L)
                .userName("newuser")
                .email("new@example.com")
                .name("New")
                .lastName("Name")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUserName("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toResponseDTO(updatedUser)).thenReturn(updatedResponseDTO);

        // When
        UserResponseDTO result = userService.updateUser(1L, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("newuser", result.getUserName());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("New", result.getName());
        assertEquals("Name", result.getLastName());
        verify(userRepository).findById(1L);
        verify(userRepository).existsByUserName("newuser");
        verify(userRepository).existsByEmail("new@example.com");
        verify(userRepository).save(any(User.class));
    }
}
