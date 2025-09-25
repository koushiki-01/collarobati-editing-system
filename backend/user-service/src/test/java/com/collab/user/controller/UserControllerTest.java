package com.collab.user.controller;

import com.collab.user.entity.User;
import com.collab.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        // Clear DB before each test
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser() throws Exception {
        String json = """
          { "username": "alice", "email": "alice@example.com", "password": "1234" }
        """;

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Insert a user into DB
        userRepository.save(new User("bob", "bob@example.com", "secret"));

        String json = """
          { "username": "bob", "password": "secret" }
        """;

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void testLoginFailure() throws Exception {
        String json = """
          { "username": "charlie", "password": "wrongpass" }
        """;

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testGetProfile() throws Exception {
        userRepository.save(new User("dave", "dave@example.com", "password"));

        mockMvc.perform(get("/api/users/dave"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("dave"))
                .andExpect(jsonPath("$.email").value("dave@example.com"));
    }

    @Test
    void testGetProfileNotFound() throws Exception {
        mockMvc.perform(get("/api/users/ghost"))
                .andExpect(status().isNotFound());
    }
}
