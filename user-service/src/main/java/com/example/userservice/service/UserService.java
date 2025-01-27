package com.example.userservice.service;

import com.example.userservice.model.UserDTO;
import org.springframework.lang.Nullable;

import java.util.List;

public interface UserService {
    List<UserDTO> getUsers();
    @Nullable
    UserDTO getUser(Long id);
    @Nullable
    UserDTO getUserByPhone(String phoneNumber);
    @Nullable
    UserDTO getUserByEmail(String email);
    UserDTO addUser(UserDTO userDTO);
    @Nullable
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}
