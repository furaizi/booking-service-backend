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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class UserServiceIT {

    @Container
    @ServiceConnection(type = JdbcConnectionDetails.class)
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    private static final List<User> TEST_USERS = List.of(
            new User(1L, "John", "Doe", "hash", "john.doe@example.com", "+1234567890", Role.USER, LocalDateTime.now(), LocalDateTime.now()),
            new User(2L, "Admin", "Admin", "admin", "admin@example.com", "+0987654321", Role.ADMIN, LocalDateTime.now(), LocalDateTime.now())
    );


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void prepare() {
        userRepository.deleteAll();
        userRepository.saveAll(TEST_USERS);
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("admin@example.com"));
    }

    @Test
    void testGetUserById_whenUserExists() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passwordHash").value("hash"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("+1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(Role.USER));
    }

    @Test
    void testGetUserById_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchUserByPhone_whenUserExists() throws Exception {
        mockMvc.perform(get("/api/users/search/phone?phoneNumber=+1234567890"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passwordHash").value("hash"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("+1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(Role.USER));
    }

    @Test
    void testSearchUserByPhone_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/search/phone?phoneNumber=+0000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchUserByEmail_whenUserExists() throws Exception {
        mockMvc.perform(get("/api/users/search/email?email=admin@example.com"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Admin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Admin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passwordHash").value("admin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("admin.example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("+0987654321"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(Role.ADMIN));
    }

    @Test
    void testSearchUserByEmail_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/search/email?email=notfound@example.com"))
                .andExpect(status().isNotFound());
    }
}
