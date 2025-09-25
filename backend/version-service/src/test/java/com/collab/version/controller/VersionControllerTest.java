package com.collab.version.controller;

import com.collab.version.entity.Version;
import com.collab.version.repository.VersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class VersionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VersionRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void testCreateVersion() throws Exception {
        String json = """
          {"documentId":1,"content":"Version 1","modifiedBy":"alice"}
        """;

        mockMvc.perform(post("/api/versions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Version 1"))
                .andExpect(jsonPath("$.modifiedBy").value("alice"));
    }

    @Test
    void testGetVersions() throws Exception {
        repository.save(new Version(1L, "V1", "bob", java.time.LocalDateTime.now()));

        mockMvc.perform(get("/api/versions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("V1"));
    }
}
