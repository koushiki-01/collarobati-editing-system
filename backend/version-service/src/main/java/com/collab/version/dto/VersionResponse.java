package com.collab.version.dto;

import java.time.LocalDateTime;

public class VersionResponse {
    private Long id;
    private Long documentId;
    private String content;
    private String modifiedBy;
    private LocalDateTime timestamp;

    public VersionResponse(Long id, Long documentId, String content, String modifiedBy, LocalDateTime timestamp) {
        this.id = id;
        this.documentId = documentId;
        this.content = content;
        this.modifiedBy = modifiedBy;
        this.timestamp = timestamp;
    }

    // Getters
    public Long getId() { return id; }
    public Long getDocumentId() { return documentId; }
    public String getContent() { return content; }
    public String getModifiedBy() { return modifiedBy; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
