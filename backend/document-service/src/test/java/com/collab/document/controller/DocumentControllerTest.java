package com.collab.document.controller;

import com.collab.document.entity.Document;
import com.collab.document.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void testCreateAndGetDoc() throws Exception {
        String json = "{\"title\":\"Doc1\",\"content\":\"Hello\",\"owner\":\"alice\"}";

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Doc1"))
                .andExpect(jsonPath("$.owner").value("alice"));

        // retrieve list
        mockMvc.perform(get("/api/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Doc1"));
    }

    @Test
    void testSaveSnapshotNoErrors() throws Exception {
        Document d = repository.save(new Document("Doc2", "content", "bob"));

        mockMvc.perform(post("/api/documents/" + d.getId() + "/save")
                .param("user", "bob"))
                .andExpect(status().isOk());
    }
}
