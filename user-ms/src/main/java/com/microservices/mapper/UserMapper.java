package com.microservices.mapper;

import com.microservices.dto.UserRequestDTO;
import com.microservices.dto.UserResponseDTO;
import com.microservices.dto.UserUpdateDTO;
import com.microservices.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    // Interface MapStruct para mapeo automático entre DTOs y entidades

    //UserRequestDTO a User entity
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "registerDate", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    //User entity a UserResponseDTO
    UserResponseDTO toResponseDTO(User user);

    //Actualiza User entity con datos de UserUpdateDTO
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "registerDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UserUpdateDTO userUpdateDTO, @MappingTarget User user); // Actualización parcial ignorando campos nulos

    //Convierte UserUpdateDTO a User entity (para casos donde se necesita crear entity completa)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "registerDate", ignore = true)
    User toEntity(UserUpdateDTO userUpdateDTO);
}
