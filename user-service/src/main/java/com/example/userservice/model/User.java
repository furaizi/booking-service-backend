package com.example.userservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public record User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id,
    @Column(nullable = false)
    String firstName,
    @Column(nullable = false)
    String lastName,
    @Column(nullable = false)
    String passwordHash,
    String email,
    @Column(length = 20, nullable = false, unique = true)
    String phone,
    Role role,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

enum Role {
    USER,
    ADMIN
}
