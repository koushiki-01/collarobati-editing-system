package com.collab.version.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "versions")
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long documentId;

    @Column(length = 5000,columnDefinition = "TEXT")
    private String content;

    private String modifiedBy;  

    private LocalDateTime timestamp;

    public Version() {}

    public Version(Long documentId, String content, String modifiedBy, LocalDateTime timestamp) {
        this.documentId = documentId;
        this.content = content;
        this.modifiedBy = modifiedBy;
        this.timestamp = timestamp;
    }

    public Version(Long documentId, String content, String modifiedBy) {
        this(documentId, content, modifiedBy, LocalDateTime.now());
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
