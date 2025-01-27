package com.example.userservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public record User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id,
    @Column(length = 50, nullable = false)
    String firstName,
    @Column(length = 50, nullable = false)
    String lastName,
    @Column(nullable = false)
    String passwordHash,
    @Column(length = 100)
    String email,
    @Column(length = 20, nullable = false, unique = true)
    String phoneNumber,
    Role role,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

enum Role {
    USER,
    ADMIN
}
