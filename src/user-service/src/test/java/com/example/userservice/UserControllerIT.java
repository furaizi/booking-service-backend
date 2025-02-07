package com.example.userservice;

import com.example.userservice.model.Role;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureMockMvc
@Testcontainers
@WithMockUser
public class UserControllerIT {

    @Container
    @ServiceConnection(type = JdbcConnectionDetails.class)
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:latest");


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void prepare() {
        userRepository.deleteAll();
    }

    @Test
    void testGetAllUsers() throws Exception {
        final var savedUsers = userRepository.saveAll(List.of(createTestUser()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(savedUsers.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(savedUsers.getFirst().getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(savedUsers.getFirst().getLastName()));
    }

    @Test
    void testGetUserById_whenUserExists() throws Exception {
        final var savedUser = userRepository.save(createTestUser());
        final var id = savedUser.getId();

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(savedUser.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(savedUser.getLastName()));
    }

    @Test
    void testGetUserById_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchUserByPhone_whenUserExists() throws Exception {
        final var savedUser = userRepository.save(createTestUser());
        final var phoneNumber = savedUser.getPhoneNumber();

        mockMvc.perform(get("/api/users/search?phone={phoneNumber}", phoneNumber))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(savedUser.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(savedUser.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(phoneNumber));
    }

    @Test
    void testSearchUserByPhone_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/search?phone=+88005553535"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchUserByEmail_whenUserExists() throws Exception {
        final var savedUser = userRepository.save(createTestUser());
        final var email = savedUser.getEmail();

        mockMvc.perform(get("/api/users/search?email={email}", email))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(savedUser.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(savedUser.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
    }

    @Test
    void testSearchUserByEmail_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/search?email=notfound@example.com"))
                .andExpect(status().isNotFound());
    }

    private User createTestUser() {
        return User.builder()
                .id(null)
                .firstName("John")
                .lastName("Doe")
                .passwordHash("hash")
                .email("john.doe@example.com")
                .role(Role.USER)
                .phoneNumber("+1234567890")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
