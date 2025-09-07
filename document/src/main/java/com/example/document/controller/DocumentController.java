package com.example.document.controller;

import com.example.document.dto.CreateDocRequest;
import com.example.document.dto.EditRequest;
import com.example.document.model.Document;
import com.example.document.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Document> createDocument(@RequestBody CreateDocRequest request) {
        return ResponseEntity.ok(documentService.createDocument(request));
    }

   @Operation(summary = "Get a document by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(
            @Parameter(description = "ID of the document", example = "1")
            @PathVariable Long id) {
        Optional<Document> doc = documentService.getDocument(id);
        return doc.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/edit")
    public ResponseEntity<Document> editDocument(@RequestBody EditRequest request) {
        Optional<Document> updated = documentService.editDocument(request);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
