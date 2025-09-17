package com.microservices.service.impl;

import com.microservices.dto.LoginDTO;
import com.microservices.dto.LoginResponseDTO;
import com.microservices.dto.UserRequestDTO;
import com.microservices.dto.UserResponseDTO;
import com.microservices.dto.UserUpdateDTO;
import com.microservices.entity.User;
import com.microservices.enums.ErrorCode;
import com.microservices.exception.InvalidCredentialsException;
import com.microservices.exception.UserAlreadyExistsException;
import com.microservices.exception.UserNotFoundException;
import com.microservices.exception.ValidationException;
import com.microservices.mapper.UserMapper;
import com.microservices.repository.UserRepository;
import com.microservices.service.JwtService;
import com.microservices.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    // Implementación del servicio de usuarios con lógica de negocio CRUD

    private final UserRepository userRepository; // Repositorio para operaciones de base de datos
    private final UserMapper userMapper; // Mapper para conversión entre DTOs y entidades
    private final PasswordEncoder passwordEncoder; // Encriptador de contraseñas
    private final JwtService jwtService; // Servicio para manejo de JWT

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        try {
            // Verificar si ya existe un usuario con el mismo userName
            if (userRepository.existsByUserName(userRequestDTO.getUserName())) {
                throw UserAlreadyExistsException.byUserName(userRequestDTO.getUserName());
            }
            
            // Verificar si ya existe un usuario con el mismo email
            if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
                throw UserAlreadyExistsException.byEmail(userRequestDTO.getEmail());
            }
            
            User user = userMapper.toEntity(userRequestDTO); // Convierte DTO a entidad
            // Encriptar la contraseña antes de guardar
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // Siempre crear como USER (ADMIN se crea/modifica en base de datos)
            user.setRole(com.microservices.entity.UserRole.USER);
            User savedUser = userRepository.save(user); // Guarda en base de datos
            return userMapper.toResponseDTO(savedUser); // Convierte entidad a DTO de respuesta
            
        } catch (UserAlreadyExistsException e) {
            // Re-lanzar excepciones de negocio sin envolver
            throw e;
        } catch (Exception e) {
            // Envolver errores inesperados en excepciones de negocio
            log.error("Error inesperado al crear usuario: {}", e.getMessage(), e);
            throw new ValidationException("Error interno al crear usuario: " + e.getMessage(), ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public UserResponseDTO getUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> UserNotFoundException.byId(id)); // Lanza excepción si no encuentra el usuario
            return userMapper.toResponseDTO(user);
            
        } catch (UserNotFoundException e) {
            // Re-lanzar excepciones de negocio sin envolver
            throw e;
        } catch (Exception e) {
            // Envolver errores inesperados en excepciones de negocio
            log.error("Error inesperado al obtener usuario con ID {}: {}", id, e.getMessage(), e);
            throw new UserNotFoundException("Error interno al obtener usuario: " + e.getMessage(), ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        try {
            // Verificar si el usuario existe
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> UserNotFoundException.byId(id));
            
            // Verificar si el nuevo userName ya existe (si se está actualizando)
            if (userUpdateDTO.getUserName() != null && 
                !userUpdateDTO.getUserName().equals(existingUser.getUserName()) &&
                userRepository.existsByUserName(userUpdateDTO.getUserName())) {
                throw UserAlreadyExistsException.byUserName(userUpdateDTO.getUserName());
            }
            
            // Verificar si el nuevo email ya existe (si se está actualizando)
            if (userUpdateDTO.getEmail() != null && 
                !userUpdateDTO.getEmail().equals(existingUser.getEmail()) &&
                userRepository.existsByEmail(userUpdateDTO.getEmail())) {
                throw UserAlreadyExistsException.byEmail(userUpdateDTO.getEmail());
            }
            
            // Actualizar la entidad con los nuevos datos
            userMapper.updateEntity(userUpdateDTO, existingUser);
            
            // Encriptar la contraseña si se está actualizando
            if (userUpdateDTO.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
            }
            
            // Guardar los cambios
            User updatedUser = userRepository.save(existingUser);
            
            return userMapper.toResponseDTO(updatedUser);
            
        } catch (UserNotFoundException | UserAlreadyExistsException e) {
            // Re-lanzar excepciones de negocio sin envolver
            throw e;
        } catch (Exception e) {
            // Envolver errores inesperados en excepciones de negocio
            log.error("Error inesperado al actualizar usuario con ID {}: {}", id, e.getMessage(), e);
            throw new UserNotFoundException("Error interno al actualizar usuario: " + e.getMessage(), ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(userMapper::toResponseDTO)
                    .toList();
                    
        } catch (Exception e) {
            // Envolver errores inesperados en excepciones de negocio
            log.error("Error inesperado al obtener todos los usuarios: {}", e.getMessage(), e);
            throw new ValidationException("Error interno al obtener usuarios: " + e.getMessage(), ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        try {
            // Verificar si el usuario existe antes de eliminarlo
            if (!userRepository.existsById(id)) {
                throw UserNotFoundException.byId(id);
            }
            userRepository.deleteById(id);
            
        } catch (UserNotFoundException e) {
            // Re-lanzar excepciones de negocio sin envolver
            throw e;
        } catch (Exception e) {
            // Envolver errores inesperados en excepciones de negocio
            log.error("Error inesperado al eliminar usuario con ID {}: {}", id, e.getMessage(), e);
            throw new UserNotFoundException("Error interno al eliminar usuario: " + e.getMessage(), ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        try {
            // Buscar usuario por nombre de usuario
            User user = userRepository.findByUserName(loginDTO.getUserName())
                    .orElseThrow(() -> InvalidCredentialsException.invalidLogin());
            
            // Verificar la contraseña usando BCrypt
            if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                throw InvalidCredentialsException.invalidLogin();
            }
            
            // Generar JWT token
            String token = jwtService.generateToken(user);
            log.debug("Token generado completo: {}", token);
            
            // Crear respuesta con JWT y datos del usuario
            return LoginResponseDTO.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .expiresIn(86400000L) // 24 horas en milisegundos
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .email(user.getEmail())
                    .name(user.getName())
                    .lastName(user.getLastName())
                    .role(user.getRole())
                    .registerDate(user.getRegisterDate())
                    .build();
            
        } catch (InvalidCredentialsException e) {
            // Re-lanzar excepciones de negocio sin envolver
            throw e;
        } catch (Exception e) {
            // Envolver errores inesperados en excepciones de negocio
            log.error("Error inesperado durante login para usuario {}: {}", loginDTO.getUserName(), e.getMessage(), e);
            throw new InvalidCredentialsException("Error interno durante autenticación: " + e.getMessage(), ErrorCode.DATABASE_ERROR);
        }
    }
}
