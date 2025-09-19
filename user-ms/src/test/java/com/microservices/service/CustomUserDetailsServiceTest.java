package com.microservices.service;

import com.microservices.entity.User;
import com.microservices.entity.UserRole;
import com.microservices.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests para CustomUserDetailsService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService Tests")
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId(1L)
                .userName("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .name("Test")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void shouldLoadUserByUsernameSuccessfully() {
        // Given
        when(userRepository.findByUserName("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        // Then
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        
        verify(userRepository).findByUserName("testuser");
    }

    @Test
    @DisplayName("Should load admin user by username successfully")
    void shouldLoadAdminUserByUsernameSuccessfully() {
        // Given
        User adminUser = User.builder()
                .userId(2L)
                .userName("admin")
                .email("admin@example.com")
                .password("adminPassword")
                .name("Admin")
                .lastName("User")
                .role(UserRole.ADMIN)
                .registerDate(LocalDateTime.now())
                .build();
        
        when(userRepository.findByUserName("admin")).thenReturn(Optional.of(adminUser));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

        // Then
        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("adminPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(userDetails.isEnabled());
        
        verify(userRepository).findByUserName("admin");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("nonexistent")
        );

        assertEquals("Usuario no encontrado: nonexistent", exception.getMessage());
        verify(userRepository).findByUserName("nonexistent");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when username is null")
    void shouldThrowUsernameNotFoundExceptionWhenUsernameIsNull() {
        // Given
        when(userRepository.findByUserName(null)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(null)
        );

        assertEquals("Usuario no encontrado: null", exception.getMessage());
        verify(userRepository).findByUserName(null);
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when username is empty")
    void shouldThrowUsernameNotFoundExceptionWhenUsernameIsEmpty() {
        // Given
        when(userRepository.findByUserName("")).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("")
        );

        assertEquals("Usuario no encontrado: ", exception.getMessage());
        verify(userRepository).findByUserName("");
    }

    @Test
    @DisplayName("Should handle user with different roles correctly")
    void shouldHandleUserWithDifferentRolesCorrectly() {
        // Given
        User userWithRole = User.builder()
                .userId(3L)
                .userName("roleuser")
                .email("role@example.com")
                .password("rolePassword")
                .name("Role")
                .lastName("User")
                .role(UserRole.USER)
                .registerDate(LocalDateTime.now())
                .build();
        
        when(userRepository.findByUserName("roleuser")).thenReturn(Optional.of(userWithRole));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("roleuser");

        // Then
        assertNotNull(userDetails);
        assertEquals("roleuser", userDetails.getUsername());
        assertEquals("rolePassword", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        
        verify(userRepository).findByUserName("roleuser");
    }
}
