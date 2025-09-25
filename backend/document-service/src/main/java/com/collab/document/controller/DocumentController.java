package com.collab.document.controller;

import com.collab.document.dto.DocumentRequest;
import com.collab.document.dto.DocumentResponse;
import com.collab.document.entity.Document;
import com.collab.document.service.DocumentService;

//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DocumentResponse> create(@RequestBody DocumentRequest req) {
        Document doc = service.createDocument(req.getTitle(), req.getContent(), req.getOwner());
        return new ResponseEntity<>(
                new DocumentResponse(doc.getId(), doc.getTitle(), doc.getContent(), doc.getOwner()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> get(@PathVariable Long id) {
        return service.getDocument(id)
                .map(d -> ResponseEntity.ok(new DocumentResponse(d.getId(), d.getTitle(), d.getContent(), d.getOwner(), d.getLastModifiedBy())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<DocumentResponse> list() {
        return service.listDocuments().stream()
                .map(d -> new DocumentResponse(d.getId(), d.getTitle(), d.getContent(), d.getOwner(),d.getLastModifiedBy()))
                .collect(Collectors.toList());
    }

    /**
     * Update document content and auto-create a single version
     * Consistent with WebSocket edits
     */
    @PostMapping("/{id}/update-content")
    public ResponseEntity<Void> updateContent(
        @PathVariable Long id,
        @RequestParam String user,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "string",
            required = true,
            content = @Content(schema = @Schema(type = "string"))
        )
        @RequestBody String content) {

        service.updateContent(id, content, user);
        return ResponseEntity.ok().build();
    }
}
