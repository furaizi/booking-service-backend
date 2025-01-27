package com.example.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String email;
    private String phoneNumber;
    private Role role;
}
