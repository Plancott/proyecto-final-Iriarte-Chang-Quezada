package com.microservices.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests para AuthorizationService - Lógica de autorización
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthorizationService Tests")
class AuthorizationServiceTest {

    @Mock
    private HttpServletRequest request;

    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        authorizationService = new AuthorizationService();
    }

    @Test
    @DisplayName("Should return true when user is admin")
    void shouldReturnTrueWhenUserIsAdmin() {
        // Given
        when(request.getAttribute("role")).thenReturn("ADMIN");

        // When
        boolean isAdmin = authorizationService.isAdmin(request);

        // Then
        assertTrue(isAdmin);
        verify(request).getAttribute("role");
    }

    @Test
    @DisplayName("Should return false when user is not admin")
    void shouldReturnFalseWhenUserIsNotAdmin() {
        // Given
        when(request.getAttribute("role")).thenReturn("USER");

        // When
        boolean isAdmin = authorizationService.isAdmin(request);

        // Then
        assertFalse(isAdmin);
    }

    @Test
    @DisplayName("Should return false when role is null")
    void shouldReturnFalseWhenRoleIsNull() {
        // Given
        when(request.getAttribute("role")).thenReturn(null);

        // When
        boolean isAdmin = authorizationService.isAdmin(request);

        // Then
        assertFalse(isAdmin);
    }

    @Test
    @DisplayName("Should return true when user is owner of resource")
    void shouldReturnTrueWhenUserIsOwnerOfResource() {
        // Given
        when(request.getAttribute("userId")).thenReturn(1L);

        // When
        boolean isOwner = authorizationService.isOwner(request, 1L);

        // Then
        assertTrue(isOwner);
        verify(request).getAttribute("userId");
    }

    @Test
    @DisplayName("Should return false when user is not owner of resource")
    void shouldReturnFalseWhenUserIsNotOwnerOfResource() {
        // Given
        when(request.getAttribute("userId")).thenReturn(1L);

        // When
        boolean isOwner = authorizationService.isOwner(request, 999L);

        // Then
        assertFalse(isOwner);
    }

    @Test
    @DisplayName("Should return false when userId is null")
    void shouldReturnFalseWhenUserIdIsNull() {
        // Given
        when(request.getAttribute("userId")).thenReturn(null);

        // When
        boolean isOwner = authorizationService.isOwner(request, 1L);

        // Then
        assertFalse(isOwner);
    }

    @Test
    @DisplayName("Should allow admin to access any resource")
    void shouldAllowAdminToAccessAnyResource() {
        // Given
        when(request.getAttribute("role")).thenReturn("ADMIN");
        when(request.getAttribute("userId")).thenReturn(1L);

        // When
        boolean canAccess = authorizationService.canAccessResource(request, 999L);

        // Then
        assertTrue(canAccess);
        verify(request).getAttribute("role");
    }

    @Test
    @DisplayName("Should allow user to access own resource")
    void shouldAllowUserToAccessOwnResource() {
        // Given
        when(request.getAttribute("role")).thenReturn("USER");
        when(request.getAttribute("userId")).thenReturn(1L);

        // When
        boolean canAccess = authorizationService.canAccessResource(request, 1L);

        // Then
        assertTrue(canAccess);
        verify(request).getAttribute("role");
        verify(request).getAttribute("userId");
    }

    @Test
    @DisplayName("Should deny user access to other user's resource")
    void shouldDenyUserAccessToOtherUserResource() {
        // Given
        when(request.getAttribute("role")).thenReturn("USER");
        when(request.getAttribute("userId")).thenReturn(1L);

        // When
        boolean canAccess = authorizationService.canAccessResource(request, 999L);

        // Then
        assertFalse(canAccess);
    }

    @Test
    @DisplayName("Should return true when user can perform admin operations")
    void shouldReturnTrueWhenUserCanPerformAdminOperations() {
        // Given
        when(request.getAttribute("role")).thenReturn("ADMIN");

        // When
        boolean canPerformAdminOps = authorizationService.canPerformAdminOperations(request);

        // Then
        assertTrue(canPerformAdminOps);
    }

    @Test
    @DisplayName("Should return false when user cannot perform admin operations")
    void shouldReturnFalseWhenUserCannotPerformAdminOperations() {
        // Given
        when(request.getAttribute("role")).thenReturn("USER");

        // When
        boolean canPerformAdminOps = authorizationService.canPerformAdminOperations(request);

        // Then
        assertFalse(canPerformAdminOps);
    }

    @Test
    @DisplayName("Should get current user id")
    void shouldGetCurrentUserId() {
        // Given
        when(request.getAttribute("userId")).thenReturn(1L);

        // When
        Long userId = authorizationService.getCurrentUserId(request);

        // Then
        assertEquals(1L, userId);
        verify(request).getAttribute("userId");
    }

    @Test
    @DisplayName("Should get current user name")
    void shouldGetCurrentUserName() {
        // Given
        when(request.getAttribute("userName")).thenReturn("testuser");

        // When
        String userName = authorizationService.getCurrentUserName(request);

        // Then
        assertEquals("testuser", userName);
        verify(request).getAttribute("userName");
    }
}
