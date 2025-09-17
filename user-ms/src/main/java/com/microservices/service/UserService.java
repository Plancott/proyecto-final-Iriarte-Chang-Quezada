package com.microservices.service;

import com.microservices.dto.LoginDTO;
import com.microservices.dto.LoginResponseDTO;
import com.microservices.dto.UserRequestDTO;
import com.microservices.dto.UserResponseDTO;
import com.microservices.dto.UserUpdateDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO); 

    UserResponseDTO getUser(Long id); 

    UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO); // Actualizar usuario

    List<UserResponseDTO> getAllUsers(); 

    void deleteUser(Long id); 

    LoginResponseDTO login(LoginDTO loginDTO); // Autenticar usuario y devolver JWT
}
