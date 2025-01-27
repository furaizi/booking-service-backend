package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.model.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserController {
    ResponseEntity<List<UserDTO>> getAllUsers();
    ResponseEntity<UserDTO> getUser(Long id);
    ResponseEntity<UserDTO> getUserByPhone(String phoneNumber);
    ResponseEntity<UserDTO> getUserByEmail(String email);
    ResponseEntity<UserDTO> createUser(UserDTO user);
    ResponseEntity<UserDTO> updateUser(Long id, UserDTO user);
    ResponseEntity<Void> deleteUser(Long id);
}
