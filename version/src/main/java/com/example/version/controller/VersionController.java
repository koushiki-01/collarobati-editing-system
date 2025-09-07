package com.example.version.controller;

import com.example.version.dto.SnapshotRequest;
import com.example.version.model.Version;
import com.example.version.service.VersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/versions")
@Tag(name = "Version API", description = "Manage document versions")
public class VersionController {

    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @Operation(summary = "Save a new snapshot for a document")
    @PostMapping
    public ResponseEntity<Version> saveSnapshot(@RequestBody SnapshotRequest request) {
        Version version = versionService.saveSnapshot(request.getDocumentId(), request.getContent());
        return ResponseEntity.ok(version);
    }

    @Operation(summary = "Get all versions of a document by documentId")
    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<Version>> getAllVersions(
            @Parameter(description = "ID of the document", example = "1")
            @PathVariable("documentId") Long documentId) {
        return ResponseEntity.ok(versionService.getAllVersionsByDocument(documentId));
    }

    @Operation(summary = "Get a specific version by id")
    @GetMapping("/{id}")
    public ResponseEntity<Version> getVersion(
            @Parameter(description = "ID of the version", example = "1")
            @PathVariable("id") Long id) {
        return versionService.getVersion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
