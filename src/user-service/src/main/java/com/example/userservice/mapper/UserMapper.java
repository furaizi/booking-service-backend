package com.example.userservice.mapper;

import com.example.userservice.model.User;
import com.example.userservice.model.UserDTO;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null)
            return null;
        return new UserDTO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getPasswordHash(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getRole()
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
