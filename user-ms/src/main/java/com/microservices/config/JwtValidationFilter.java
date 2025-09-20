package com.microservices.config;

import com.microservices.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;

        // Verificar si el header Authorization existe y empieza con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No hay token, continuar (los endpoints públicos no requieren token)
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // Extraer el token sin "Bearer "
        
        try {
            userName = jwtService.extractUserName(jwt);
            
            // Validar token básico (sin UserDetails)
            if (userName != null && !jwtService.isTokenExpired(jwt)) {
                // Extraer rol del token
                String role = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));
                Long userId = jwtService.extractClaim(jwt, claims -> claims.get("userId", Long.class));
                
                // Agregar información del usuario al request
                request.setAttribute("userName", userName);
                request.setAttribute("role", role);
                request.setAttribute("userId", userId);
                
                log.debug("Usuario autenticado: {} con rol: {}", userName, role);
            }
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // No filtrar endpoints públicos
        String path = request.getRequestURI();
        String method = request.getMethod();
        return path.equals("/api/users/login") || 
               (path.equals("/api/users") && "POST".equals(method)) || // Crear usuario (POST)
               path.startsWith("/h2-console");
    }
}
