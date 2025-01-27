package com.example.userservice.mapper;

import com.example.userservice.model.User;
import com.example.userservice.model.UserDTO;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
            user.id(),
            user.firstName(),
            user.lastName(),
            user.passwordHash(),
            user.email(),
            user.phoneNumber(),
            user.role()
        );
    }

    public static User toEntity(UserDTO userDTO) {
        return new User(
            userDTO.getId(),
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getPasswordHash(),
            userDTO.getEmail(),
            userDTO.getPhoneNumber(),
            userDTO.getRole(),
            null,
            null
        );
    }
}
