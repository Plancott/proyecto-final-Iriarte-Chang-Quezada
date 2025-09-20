package com.microservices.config;

import com.microservices.client.AuthClient;
import com.microservices.client.TokenValidationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoJwtValidationFilter extends OncePerRequestFilter {

    private final AuthClient authClient;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");

        // Verificar si el header Authorization existe y empieza con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No hay token, continuar (los endpoints públicos no requieren token)
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Validar token automáticamente con auth-ms
            TokenValidationResponse validation = authClient.validateToken(authHeader);
            
            if (validation.isValid()) {
                // Agregar información del usuario al request
                request.setAttribute("userName", validation.getUserName());
                request.setAttribute("role", validation.getRole());
                request.setAttribute("userId", validation.getUserId());
                
                // Establecer autenticación en Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    validation.getUserName(),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + validation.getRole()))
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                log.debug("Token válido para usuario: {} con rol: {} y userId: {}", 
                    validation.getUserName(), validation.getRole(), validation.getUserId());
                log.debug("Request attributes set - userName: {}, role: {}, userId: {}", 
                    request.getAttribute("userName"), request.getAttribute("role"), request.getAttribute("userId"));
                log.debug("Authentication set in SecurityContext for user: {}", validation.getUserName());
            } else {
                log.warn("Token inválido en request: {}", request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Token inválido\"}");
                return;
            }
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Error validando token\"}");
            return;
        }
        
        log.debug("Continuando al siguiente filtro/controlador para: {}", request.getRequestURI());
        filterChain.doFilter(request, response);
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // No filtrar endpoints públicos
        String path = request.getRequestURI();
        String method = request.getMethod();
        return path.startsWith("/h2-console") ||
               path.startsWith("/api/products/search") ||
               path.startsWith("/api/products/by-status") ||
               path.startsWith("/api/products/by-category") ||
               path.startsWith("/api/products/by-brand") ||
               path.startsWith("/api/products/by-price-range") ||
               path.startsWith("/api/products/active") ||
               path.startsWith("/api/categories") ||
               path.startsWith("/api/brands") ||
               (path.equals("/api/products") && "GET".equals(method)); // Solo GET a /api/products (listar)
    }
}
