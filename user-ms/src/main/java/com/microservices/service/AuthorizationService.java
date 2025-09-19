package com.microservices.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {

    /**
     * Verifica si el usuario actual es administrador
     */
    public boolean isAdmin(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        log.debug("Verificando si es admin. Role: {}", role);
        return "ADMIN".equals(role);
    }

    /**
     * Verifica si el usuario actual es el propietario del recurso
     */
    public boolean isOwner(HttpServletRequest request, Long resourceUserId) {
        Long userId = (Long) request.getAttribute("userId");
        return userId != null && userId.equals(resourceUserId);
    }

    /**
     * Verifica si el usuario puede acceder al recurso
     */
    public boolean canAccessResource(HttpServletRequest request, Long resourceUserId) {
        boolean isAdmin = isAdmin(request);
        boolean isOwner = isOwner(request, resourceUserId);
        log.debug("canAccessResource - isAdmin: {}, isOwner: {}, resourceUserId: {}", isAdmin, isOwner, resourceUserId);
        return isAdmin || isOwner;
    }

    /**
     * Verifica si el usuario puede realizar operaciones administrativas
     */
    public boolean canPerformAdminOperations(HttpServletRequest request) {
        return isAdmin(request);
    }

    /**
     * Obtiene el ID del usuario actual
     */
    public Long getCurrentUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    /**
     * Obtiene el nombre del usuario actual
     */
    public String getCurrentUserName(HttpServletRequest request) {
        return (String) request.getAttribute("userName");
    }
}
