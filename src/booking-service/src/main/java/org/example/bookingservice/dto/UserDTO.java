package org.example.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.bookingservice.model.Role;

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
