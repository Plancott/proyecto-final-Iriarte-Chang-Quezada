package com.microservices.util;

import com.microservices.dto.LoginDTO;
import com.microservices.dto.LoginResponseDTO;
import com.microservices.dto.UserRequestDTO;
import com.microservices.dto.UserResponseDTO;
import com.microservices.dto.UserUpdateDTO;
import com.microservices.entity.User;
import com.microservices.entity.UserRole;

import java.time.LocalDateTime;

/**
 * Builder de datos de prueba para tests
 */
public class TestDataBuilder {

    public static class UserBuilder {
        private Long userId = 1L;
        private String userName = "testuser";
        private String email = "test@example.com";
        private String password = "password123";
        private String name = "Test";
        private String lastName = "User";
        private UserRole role = UserRole.USER;
        private LocalDateTime registerDate = LocalDateTime.now();

        public UserBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder role(UserRole role) {
            this.role = role;
            return this;
        }

        public UserBuilder registerDate(LocalDateTime registerDate) {
            this.registerDate = registerDate;
            return this;
        }

        public User build() {
            return User.builder()
                    .userId(userId)
                    .userName(userName)
                    .email(email)
                    .password(password)
                    .name(name)
                    .lastName(lastName)
                    .role(role)
                    .registerDate(registerDate)
                    .build();
        }
    }

    public static class UserRequestDTOBuilder {
        private String userName = "testuser";
        private String email = "test@example.com";
        private String password = "password123";
        private String name = "Test";
        private String lastName = "User";
        private UserRole role = UserRole.USER;

        public UserRequestDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserRequestDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserRequestDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserRequestDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserRequestDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserRequestDTOBuilder role(UserRole role) {
            this.role = role;
            return this;
        }

        public UserRequestDTO build() {
            return UserRequestDTO.builder()
                    .userName(userName)
                    .email(email)
                    .password(password)
                    .name(name)
                    .lastName(lastName)
                    .role(role)
                    .build();
        }
    }

    public static class UserResponseDTOBuilder {
        private Long userId = 1L;
        private String userName = "testuser";
        private String email = "test@example.com";
        private String name = "Test";
        private String lastName = "User";
        private UserRole role = UserRole.USER;
        private LocalDateTime registerDate = LocalDateTime.now();

        public UserResponseDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserResponseDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserResponseDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserResponseDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserResponseDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserResponseDTOBuilder role(UserRole role) {
            this.role = role;
            return this;
        }

        public UserResponseDTOBuilder registerDate(LocalDateTime registerDate) {
            this.registerDate = registerDate;
            return this;
        }

        public UserResponseDTO build() {
            return UserResponseDTO.builder()
                    .userId(userId)
                    .userName(userName)
                    .email(email)
                    .name(name)
                    .lastName(lastName)
                    .role(role)
                    .registerDate(registerDate)
                    .build();
        }
    }

    public static class LoginDTOBuilder {
        private String userName = "testuser";
        private String password = "password123";

        public LoginDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public LoginDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public LoginDTO build() {
            return LoginDTO.builder()
                    .userName(userName)
                    .password(password)
                    .build();
        }
    }

    public static class LoginResponseDTOBuilder {
        private String token = "test-jwt-token";
        private String tokenType = "Bearer";
        private Long expiresIn = 3600000L;
        private Long userId = 1L;
        private String userName = "testuser";
        private String email = "test@example.com";
        private String name = "Test";
        private String lastName = "User";
        private UserRole role = UserRole.USER;
        private LocalDateTime registerDate = LocalDateTime.now();

        public LoginResponseDTOBuilder token(String token) {
            this.token = token;
            return this;
        }

        public LoginResponseDTOBuilder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public LoginResponseDTOBuilder expiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public LoginResponseDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public LoginResponseDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public LoginResponseDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public LoginResponseDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public LoginResponseDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public LoginResponseDTOBuilder role(UserRole role) {
            this.role = role;
            return this;
        }

        public LoginResponseDTOBuilder registerDate(LocalDateTime registerDate) {
            this.registerDate = registerDate;
            return this;
        }

        public LoginResponseDTO build() {
            return LoginResponseDTO.builder()
                    .token(token)
                    .tokenType(tokenType)
                    .expiresIn(expiresIn)
                    .userId(userId)
                    .userName(userName)
                    .email(email)
                    .name(name)
                    .lastName(lastName)
                    .role(role)
                    .registerDate(registerDate)
                    .build();
        }
    }

    public static class UserUpdateDTOBuilder {
        private String userName = "newuser";
        private String email = "new@example.com";
        private String password = "newpassword123";
        private String name = "New";
        private String lastName = "Name";

        public UserUpdateDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserUpdateDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserUpdateDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserUpdateDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserUpdateDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserUpdateDTO build() {
            return UserUpdateDTO.builder()
                    .userName(userName)
                    .email(email)
                    .password(password)
                    .name(name)
                    .lastName(lastName)
                    .build();
        }
    }

    // Métodos estáticos para crear builders
    public static UserBuilder user() {
        return new UserBuilder();
    }

    public static UserRequestDTOBuilder userRequest() {
        return new UserRequestDTOBuilder();
    }

    public static UserResponseDTOBuilder userResponse() {
        return new UserResponseDTOBuilder();
    }

    public static LoginDTOBuilder login() {
        return new LoginDTOBuilder();
    }

    public static LoginResponseDTOBuilder loginResponse() {
        return new LoginResponseDTOBuilder();
    }

    public static UserUpdateDTOBuilder userUpdate() {
        return new UserUpdateDTOBuilder();
    }
}
