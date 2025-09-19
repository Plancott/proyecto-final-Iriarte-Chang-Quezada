package com.microservices.controller;

import com.microservices.dto.LoginDTO;
import com.microservices.dto.LoginResponseDTO;
import com.microservices.dto.UserRequestDTO;
import com.microservices.dto.UserResponseDTO;
import com.microservices.dto.UserUpdateDTO;
import com.microservices.service.AuthorizationService;
import com.microservices.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthorizationService authorizationService; 

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        // Crear usuario libre (sin token) - siempre se crea como USER
        UserResponseDTO user = userService.createUser(userRequestDTO); // Crea usuario y retorna respuesta
        return new ResponseEntity<>(user, HttpStatus.CREATED); // Retorna 201 Created
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id, HttpServletRequest request) {
        log.debug("=== GET /api/users/{} ===", id);
        log.debug("Request attributes - userName: {}, role: {}, userId: {}", 
            request.getAttribute("userName"), 
            request.getAttribute("role"), 
            request.getAttribute("userId"));
        log.debug("Authorization header: {}", request.getHeader("Authorization"));
        
        // Solo ADMIN puede ver cualquier usuario, USER solo puede verse a sí mismo
        boolean canAccess = authorizationService.canAccessResource(request, id);
        log.debug("canAccessResource result: {}", canAccess);
        
        if (!canAccess) {
            log.warn("Access denied for user {} to resource {}", 
                request.getAttribute("userId"), id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        UserResponseDTO user = userService.getUser(id); // Obtiene usuario por ID
        log.debug("User found: {}", user);
        return ResponseEntity.ok(user); // Retorna 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO, HttpServletRequest request) {
        log.debug("=== PUT /api/users/{} ===", id);
        log.debug("Request attributes - userName: {}, role: {}, userId: {}", 
            request.getAttribute("userName"), 
            request.getAttribute("role"), 
            request.getAttribute("userId"));
        
        // Solo ADMIN puede actualizar cualquier usuario, USER solo puede actualizarse a sí mismo
        boolean canAccess = authorizationService.canAccessResource(request, id);
        log.debug("canAccessResource result: {}", canAccess);
        
        if (!canAccess) {
            log.warn("Access denied for user {} to resource {}", 
                request.getAttribute("userId"), id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO); // Actualiza usuario por ID
        return ResponseEntity.ok(updatedUser); // Retorna 200 OK con el usuario actualizado
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(HttpServletRequest request) {
        log.debug("=== GET /api/users ===");
        log.debug("Request attributes - userName: {}, role: {}, userId: {}", 
            request.getAttribute("userName"), 
            request.getAttribute("role"), 
            request.getAttribute("userId"));
        
        // Solo ADMIN puede ver todos los usuarios
        boolean canPerformAdminOps = authorizationService.canPerformAdminOperations(request);
        log.debug("canPerformAdminOperations result: {}", canPerformAdminOps);
        
        if (!canPerformAdminOps) {
            log.warn("Access denied for user {} to admin operations", 
                request.getAttribute("userId"));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<UserResponseDTO> users = userService.getAllUsers(); // Obtiene todos los usuarios
        return ResponseEntity.ok(users); // Retorna 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        log.debug("=== DELETE /api/users/{} ===", id);
        log.debug("Request attributes - userName: {}, role: {}, userId: {}", 
            request.getAttribute("userName"), 
            request.getAttribute("role"), 
            request.getAttribute("userId"));
        
        // Solo ADMIN puede eliminar usuarios
        boolean canPerformAdminOps = authorizationService.canPerformAdminOperations(request);
        log.debug("canPerformAdminOperations result: {}", canPerformAdminOps);
        
        if (!canPerformAdminOps) {
            log.warn("Access denied for user {} to admin operations", 
                request.getAttribute("userId"));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        userService.deleteUser(id); // Elimina usuario por ID
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginResponseDTO loginResponse = userService.login(loginDTO); // Autentica usuario y devuelve JWT
        return ResponseEntity.ok(loginResponse); // Retorna 200 OK con JWT y datos del usuario
    }
}
