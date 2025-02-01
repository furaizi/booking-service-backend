package com.example.userservice.controller;

import com.example.userservice.model.UserDTO;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing user entities")
public class UserControllerImpl {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get paginated users", description = "Retrieve a paginated list of all users")
    @Parameter(name = "page", description = "Page number (zero-based)", example = "0")
    @Parameter(name = "size", description = "Number of items per page", example = "10")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user list")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user details by their unique identifier")
    @ApiResponse(responseCode = "200", description = "User found successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable @Parameter(description = "User ID", example = "123") Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search user", description = "Search user by either phone number or email address")
    @ApiResponse(responseCode = "200", description = "User found successfully")
    @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<?> searchUser(
            @RequestParam(required = false) @Parameter(description = "Phone number") String phone,
            @RequestParam(required = false) @Parameter(description = "Email address") String email) {

        if (phone != null && email != null) {
            return ResponseEntity.badRequest().body("Provide either phone or email, not both");
        }
        if (phone == null && email == null) {
            return ResponseEntity.badRequest().body("Either phone or email must be provided");
        }

        var user = (phone != null)
                ? userService.getUserByPhone(phone)
                : userService.getUserByEmail(email);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Register a new user in the system")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    public ResponseEntity<UserDTO> createUser(
            @Valid @RequestBody @Parameter(description = "User data") UserDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update existing user information")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable @Parameter(description = "User ID", example = "123") Long id,
            @Valid @RequestBody @Parameter(description = "Updated user data") UserDTO user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Remove a user from the system")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Parameter(description = "User ID", example = "123") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}